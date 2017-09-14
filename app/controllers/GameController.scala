package controllers

import javax.inject._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._
import scala.concurrent.Future

trait NewGameForm {
   val newGameForm = Form(
      tuple (
         "playername" -> nonEmptyText(maxLength = 250),
         "companyname" -> nonEmptyText(maxLength = 250),
         "companylocation" -> nonEmptyText(maxLength = 50)
      )
   )
}

@Singleton
class GameController @Inject() (val messagesApi: MessagesApi)(implicit val webJarAssets: WebJarAssets) extends Controller with WithWebJarAssets with I18nSupport with NewGameForm with WithLogging {

   val startingLocations = List("hamburg","marseille","rotterdam")

   def newGame = Action {
      Ok(views.html.game.newGame(startingLocations))
   }

   def startNewGame = Action { implicit request =>

      newGameForm.bindFromRequest.fold(
         errors => {
            logger.warn("New game failed: " + errors)
            BadRequest
            // TODO (views.html.wishlist.createwishlist(errors))
         },{
            case (playerName, companyName, companyLocation) =>
               Ok(views.html.game.firstPage())
         }
      )
   }

   def mainPage = Action {
      Ok(views.html.game.mainPage())
   }

   def continueGame = TODO

}
