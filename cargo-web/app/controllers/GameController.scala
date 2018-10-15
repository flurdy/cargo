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
class GameController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc)  with I18nSupport with NewGameForm with WithLogging {

   val startingLocations = List("hamburg","marseille","rotterdam")

   // implicit def messages(request: ): Messages: 

   def showNewGameForm = Action { implicit request =>
      Ok(views.html.game.newGame(startingLocations))
   }

   def submitNewGameDetails = Action { implicit request =>

      newGameForm.bindFromRequest.fold(
         errors => {
            logger.warn("New game failed: " + errors)
            BadRequest
            // TODO (views.html.wishlist.createwishlist(errors))
         },{
            case (playerName, companyName, companyLocation) =>
            logger.warn("New game started")
               Ok(views.html.game.welcomePage())
         }
      )
   }

   def mainPage = Action { implicit request =>
      Ok(views.html.game.mainPage())
   }

   def continueGame = TODO

}
