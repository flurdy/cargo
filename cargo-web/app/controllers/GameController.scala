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

   def showNewGameForm = Action { implicit request =>
      Ok(views.html.game.newGame(startingLocations))
   }

   def submitNewGameDetails = Action { implicit request =>

      newGameForm.bindFromRequest.fold(
         errors => {
            logger.warn("New game failed: " + errors)
            BadRequest
         },{
            case (playerName, companyName, companyLocation) =>
               logger.warn("New game started")
               Ok(views.html.game.welcomePage())
         }
      )
   }

   def startGame = Action { implicit request =>
      Redirect(routes.TutorialController.showFirstJob())
   }

   def continueGame = Action { implicit request =>
      logger.warn("Game continued")
      Ok(views.html.game.tutorial.firstJob())
   }
}


@Singleton
class TutorialController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc)  with I18nSupport with NewGameForm with WithLogging {

   def showFirstJob = Action { implicit request =>
      Ok(views.html.game.tutorial.firstJob())
   }

   def showFirstJobDetails = Action { implicit request =>
      Ok(views.html.game.tutorial.firstJobDetails())
   }

   def takeFirstJob = Action { implicit request =>
      Redirect(routes.TutorialController.showFirstTruck())
   }

   def showFirstTruck = Action { implicit request =>
      Ok(views.html.game.tutorial.firstTruck())
   }

   def showFirstTruckDetails = Action { implicit request =>
      Ok(views.html.game.tutorial.firstTruckDetails())
   }

   def cantAffordFirstTruck = Action { implicit request =>
      Ok(views.html.game.tutorial.firstBankLoan())
   }

   def showFirstBankLoan = Action { implicit request =>
      Ok(views.html.game.tutorial.firstBankLoan())
   }

   def showFirstBankLoanDetails = Action { implicit request =>
      Ok(views.html.game.tutorial.firstBankLoanDetails())
   }

   def takeOutFirstBankLoan = Action { implicit request =>
      Redirect(routes.TutorialController.showFirstTruckAgain())
   }

   def showFirstTruckAgain = Action { implicit request =>
      Ok(views.html.game.tutorial.firstTruckAgain())
   }

   def showFirstTruckDetailsAgain = Action { implicit request =>
      Ok(views.html.game.tutorial.firstTruckDetailsAgain())
   }

   def buyFirstTruck = TODO

   def garage = TODO

   def mechanic = TODO

   def driver = TODO

}
