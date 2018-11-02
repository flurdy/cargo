package controllers

import javax.inject._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import models._
import repositories._

trait NewGameForm {
   val newGameForm = Form(
      tuple (
         "playername" -> nonEmptyText(maxLength = 250),
         "companyname" -> nonEmptyText(maxLength = 250)
      )
   )
}

trait IncludeGameContext {

   implicit def gameContext[A](implicit request: Request[A]): GameContext = {
      val bankLoanOffer = BankLoanOffer(new MoneyAmount("100000"), new NumberAmount("3.4"), 24)
      val bankLoan = BankLoan(GameDate(1985,8,1), bankLoanOffer, new MoneyAmount("-98783"))
      GameContext( GameDate(1985,8,1), CompanyContext("Trucks R'Us", new MoneyAmount("251120"), BankLoans(Set(bankLoan))))
   }

}

@Singleton
class GameController @Inject() (cc: ControllerComponents)(implicit ec: ExecutionContext, repositoryRegistry: RepositoryRegistry)
  extends AbstractController(cc)  with I18nSupport with NewGameForm with WithLogging with IncludeGameContext {

   val startingLocations = List("hamburg","marseille","rotterdam")

   def showNewGameForm = Action { implicit request =>
      Ok(views.html.game.newGame(startingLocations))
   }

   def submitNewGameDetails = Action.async { implicit request =>

      newGameForm.bindFromRequest.fold(
         errors => {
            logger.warn("New game failed: " + errors)
            Future.successful( BadRequest )
         },{
            case (playerName, companyName) =>
               logger.info("New game started")
               new Player(playerName).save.flatMap { player =>
                  logger.debug(s"New player saved: ${player.playerId}")
                  new Company(companyName, player).save.map { company =>
                     logger.debug(s"New company saved: ${company.companyId} ${company.ownerId}")
                     Redirect(routes.GameController.showWelcome())
                  }
               }
         }
      )
   }

   def showWelcome = Action.async { implicit request =>
      repositoryRegistry.companyRepository.get.findById(2).map {
         case Some(company) =>
            logger.debug(s"Found ${company.companyId}")
         case _ =>
            logger.debug(s"Found no company")
      }.map { _ =>
         Ok(views.html.game.welcomePage())
      }
   }

   def startGame = Action { implicit request =>
      Redirect(routes.TutorialController.startTutorial())
   }

   def continueGame = Action { implicit request =>
      logger.warn("Game continued")
      Redirect(routes.GameController.showGame())
   }

   def showGame = Action { implicit request =>
      Ok(views.html.game.mainWindow())
   }

   def mechanic = TODO
   def driver = TODO
   def ship = TODO
   def train = TODO
   def aeroplane = TODO

}
