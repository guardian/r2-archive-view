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
    (JsPath \ "body").write[String]
  )(unlift(model.Article.unapply))

  private def loadArticle(id : Int) : Option[model.Article] = {
    //TODO: this is a hack etc
    DB.withConnection { conn =>
      val stmt = conn.createStatement

      val rs = stmt.executeQuery(s"SELECT body from article_live_mgrbu where id = ${id}")
      if (rs.next()) Some(Article(id.toInt, rs.getString(1)));
      else None
    }
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
