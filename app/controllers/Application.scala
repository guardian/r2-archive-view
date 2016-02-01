package controllers

import play.api._
import play.api.mvc._
import services.aws.AwsInstanceTags

object Application extends Controller with AwsInstanceTags{

  lazy val stage = readTag("Stage").getOrElse("local")

  def index = Action {
    Ok(views.html.index(stage))
  }


  def healthcheck = Action {
    Ok("healthcheck is OK")
  }

}