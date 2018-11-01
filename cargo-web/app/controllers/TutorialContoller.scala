package controllers

import javax.inject._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._
import scala.concurrent.Future
import models._


@Singleton
class TutorialController @Inject() (cc: ControllerComponents)
  extends AbstractController(cc)  with I18nSupport with NewGameForm with WithLogging with IncludeGameContext {

   def startTutorial = Action { implicit request =>
      logger.info("Tutorial started")
      Redirect(routes.TutorialController.showFirstJob())
   }

   def showFirstJob = Action { implicit request =>
      Ok(views.html.game.tutorial.firstJob())
   }

   def showFirstJobDetails = Action { implicit request =>
      val jobDetails: JobDetails = JobDetails(
         "Jonny Import Export PLC",
         "Scrap metal",
         "Cologne",
         "Frankfurt",
         44,
         new MoneyAmount("3200"),
         GameDate(1985,8,3))
      Ok(views.html.game.tutorial.firstJobDetails(jobDetails))
   }

   def takeFirstJob = Action { implicit request =>
      logger.info("First job taken")
      Redirect(routes.TutorialController.showFirstTruck())
   }

   def showFirstTruck = Action { implicit request =>
      Ok(views.html.game.tutorial.firstTruck())
   }

   def showFirstTruckDetails = Action { implicit request =>
   val truck = TruckSaleDetails(
      TruckDetails(
         "Scania Streamline",
         "4x2 wheelset",
         440,
         "left",
         8,
         389239,
         "ok",
         false ),
      new MoneyAmount("89900"))
      Ok(views.html.game.tutorial.firstTruckDetails(truck))
   }

   def cantAffordFirstTruck = Action { implicit request =>
      Ok(views.html.game.tutorial.firstBankLoan())
   }

   def showFirstBankLoan = Action { implicit request =>
      Ok(views.html.game.tutorial.firstBankLoan())
   }

   def showFirstBankLoanDetails = Action { implicit request =>
   val bankLoanOffer = BankLoanOffer(new MoneyAmount("100000"), new NumberAmount("3.4"), 36)
      Ok(views.html.game.tutorial.firstBankLoanDetails(bankLoanOffer))
   }

   def takeOutFirstBankLoan = Action { implicit request =>
      logger.info("Bank loan borrowed")
      Redirect(routes.TutorialController.showFirstTruckAgain())
   }

   def showFirstTruckAgain = Action { implicit request =>
      Ok(views.html.game.tutorial.firstTruckAgain())
   }

   def showFirstTruckDetailsAgain = Action { implicit request =>
      val truck = TruckSaleDetails(
         TruckDetails(
            "Scania Streamline",
            "4x2 wheelset",
            440,
            "left",
            8,
            389239,
            "ok",
            false ),
         new MoneyAmount("89900"))
      Ok(views.html.game.tutorial.firstTruckDetailsAgain(truck))
   }

   def buyFirstTruck =  Action { implicit request =>
      logger.info("First truck bought")
      Redirect(routes.TutorialController.showStartJob())
   }

   def showStartJob = Action { implicit request =>
      Ok(views.html.game.tutorial.startJob())
   }

   def startJob = Action { implicit request =>
      Redirect(routes.TutorialController.showDeliverJob())
   }

   def showDeliverJob = Action { implicit request =>
      Ok(views.html.game.tutorial.deliverJob())
   }

   def deliverJob = Action { implicit request =>
      logger.info("First job delivered")
      Redirect(routes.TutorialController.showJobDelivered())
   }

   def showJobDelivered = Action { implicit request =>
      Ok(views.html.game.tutorial.jobDelivered())
   }

   def showJobDeliveredDetails = Action { implicit request =>
      val jobDetails: JobDetails = JobDetails(
         "Jonny Import Export PLC",
         "Scrap metal",
         "Cologne",
         "Frankfurt",
         44,
         new MoneyAmount("3200"),
         GameDate(1985,8,3))
      val jobDelivered = JobDelivered(jobDetails, 15, DeliveryExpenses(new MoneyAmount(-650), new MoneyAmount(-150)))
      Ok(views.html.game.tutorial.jobDeliveredDetails(jobDelivered))
   }

   def invoiceJob = Action { implicit request =>
      logger.info("First job invoiced")
      Redirect(routes.TutorialController.showFirstGarage())
   }

   def showFirstGarage = Action { implicit request =>
      Ok(views.html.game.tutorial.firstGarage())
   }

   def showFirstGarageDetails = Action { implicit request =>
      val garage = GarageDetails("Franktfurt", 2)
      val garageOffer = GarageSaleDetails( garage, new MoneyAmount(59300) )
      Ok(views.html.game.tutorial.firstGarageDetails(garageOffer))
   }

   def buyGarage = Action { implicit request =>
      logger.info("Buy garage")
      Redirect(routes.TutorialController.showEndOfTutorial())
   }

   def showEndOfTutorial =  Action { implicit request =>
      Ok(views.html.game.tutorial.endOfTutorial())
   }

   def endTutorial = Action { implicit request =>
      logger.info("End of tutorial")
      Redirect(routes.GameController.showGame())
   }

}
