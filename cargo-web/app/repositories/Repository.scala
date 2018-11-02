package repositories

import com.google.inject.ImplementedBy
import javax.inject.{Inject, Provider, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import models._


@ImplementedBy(classOf[DefaultRepositoryRegistry])
trait RepositoryRegistry {
   def playerRepository:  Provider[PlayerRepository]
   def companyRepository: Provider[CompanyRepository]
}

@Singleton
class DefaultRepositoryRegistry @Inject() (
   val playerRepository:  Provider[PlayerRepository],
   val companyRepository: Provider[CompanyRepository]
) extends RepositoryRegistry

trait Repository[A] {

   def save(a: A): Future[A]

}

trait Lookup[A] {

   def findById(id: Long): Future[Option[A]]

}

@ImplementedBy(classOf[DefaultPlayerRepository])
trait PlayerRepository
extends HasDatabaseConfigProvider[PostgresProfile]
with Lookup[Player] with Repository[Player] {

   val players = TableQuery[PlayerTable]

   class PlayerTable(tag: Tag) extends Table[Player](tag, "player") {

      def playerId = column[Long]("player_id", O.PrimaryKey)
      def fullname = column[String]("fullname")

      def * = (playerId.?, fullname) <> (Player.tupled, Player.unapply)

   }

   def save(player: Player): Future[Player] =
      db.run (
            ( players returning players.map(_.playerId)
               into ((player, playerId) => player.copy(playerId = Some(playerId)))
            ) += player
      )

   def findById(playerId: Long): Future[Option[Player]] =
      db.run(
         players.filter(_.playerId === playerId).take(1).result.headOption
      )
}

@Singleton
class DefaultPlayerRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
(implicit val executionContext: ExecutionContext) extends PlayerRepository


@ImplementedBy(classOf[DefaultCompanyRepository])
trait CompanyRepository
extends HasDatabaseConfigProvider[PostgresProfile]
with Lookup[Company] with Repository[Company] {

   def playerRepository: PlayerRepository

   private val companies = TableQuery[CompanyTable]

   private class CompanyTable(tag: Tag) extends Table[Company](tag, "company") {

      def companyId =   column[Long]("company_id", O.PrimaryKey)
      def companyName = column[String]("company_name")
      def ownerId =     column[Long]("owner_id")

      def owner =       foreignKey("owner_id", ownerId, playerRepository.players)(
         _.playerId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)

      def * = (companyId.?, companyName, ownerId.?) <> (Company.tupled, Company.unapply)

   }

   def save(company: Company): Future[Company] =
      db.run (
            ( companies returning companies.map(_.companyId)
               into ((company, companyId) => company.copy(companyId = Some(companyId)))
            ) += company
      )

   def findById(companyId: Long): Future[Option[Company]] =
      db.run(
         companies.filter(_.companyId === companyId).take(1).result.headOption
      )

}

@Singleton
class DefaultCompanyRepository @Inject()
(protected val dbConfigProvider: DatabaseConfigProvider, val playerRepository: PlayerRepository)
(implicit val executionContext: ExecutionContext) extends CompanyRepository






/*
import anorm._
import anorm.SqlParser._
import play.api.db.{DBApi, Database}
import scala.concurrent.{ExecutionContext, Future}


trait RepositoryLookup {

   def dbApi: DBApi

   lazy val db: Database = dbApi.database("default")

   implicit def ec: ExecutionContext

}

trait Repository extends RepositoryLookup {

   def sequence: String

   def generateNextId()(implicit ec: ExecutionContext) =
      Future{
         db.withConnection{ implicit connection =>
            SQL"""SELECT NEXTVAL($sequence)""".as(scalar[Long].single)
         }
      }

}
*/
