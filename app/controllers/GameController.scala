package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.i18n.{MessagesApi, I18nSupport}

@Singleton
class GameController @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def newGame = TODO
  
  def continueGame = TODO

}
