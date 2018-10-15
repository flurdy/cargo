package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.i18n.{MessagesApi, I18nSupport}

// trait WithWebJarAssets {
//    implicit def webJarAssets: WebJarAssets
   // implicit def request2WebJarAssets(implicit request: RequestHeader): WebJarAssets = webJarAssets
// }

trait WithLogging {

   val logger: Logger = Logger(this.getClass())

}


@Singleton
class HomeController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc) with I18nSupport {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

}
