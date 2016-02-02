package controllers

import play.api._
import play.api.db._
import play.api.mvc._
import services.aws.AwsInstanceTags

object Application extends Controller with AwsInstanceTags{

  lazy val stage = readTag("Stage").getOrElse("local")

  def index = Action {
    Ok(views.html.index(stage))
  }

  import play.api.Play.current

  private def getDbHealth : Boolean = {
    DB.withConnection { conn =>
      // do whatever you need with the connection
      val conn = DB.getConnection()
      try {
        val stmt = conn.createStatement
        val rs = stmt.executeQuery("SELECT * from Migrated_Content where rownum=1")
        if (rs.next()) true
        else false
      } finally {
        conn.close()
      }
    }
  }


  def healthcheck = Action {
    val healthCheck : List[Boolean] = getDbHealth :: Nil
    healthCheck.foldLeft(true)(_ && _) match {
      case true => Ok("healthcheck is OK")
      case false => InternalServerError("healthcheck failed")
    }
  }

}