package controllers


import controllers.Application._
import model._
import play.api.mvc.Action
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.db._


object ArticleController {
  import play.api.Play.current


  implicit val articleWrites: Writes[model.Article] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "headline").write[String] and
    (JsPath \ "body").write[String]
  )(unlift(model.Article.unapply))


  private def loadArchiveField(sql : String) : Option[String] = {
    DB.withConnection { conn =>
      val stmt = conn.createStatement
      val rs = stmt.executeQuery(sql)
      if (rs.next()) Some(rs.getString(1));
      else None
    }
  }

  private def loadArchiveBody(id : Int) = loadArchiveField(s"SELECT body from article_live_mgrbu where id = ${id}")

  private def loadArchiveHeadline(id : Int) = loadArchiveField(s"SELECT headline from content_live_mgrbu where id = ${id}")


  private def loadArticle(id : Int) : Option[model.Article] = {
    for(  headline <-loadArchiveHeadline(id);
          body <- loadArchiveBody(id))
      yield Article(id, headline, body)
  }


  def getArticle(id: String) = Action {
    loadArticle(id.toInt) match {
      case Some(article) => Ok(Json.toJson(article))
      case None => NotFound
    }
  }

  def index = Action {
    Ok(views.html.article(stage))
  }


}
