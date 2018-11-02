package models

import java.text.NumberFormat
import java.time.LocalDateTime
import scala.concurrent.Future
import repositories._

case class GameDate(year: Int, month: Int, week: Int)

trait FormattedAmount {
   def value: BigDecimal
   lazy private val formatter = NumberFormat.getInstance
   lazy val formatted = formatter.format(value)
}

case class NumberAmount(value: BigDecimal) extends FormattedAmount {
   def this(value: String) = this(BigDecimal(value))
}

case class MoneyAmount(value: BigDecimal) extends FormattedAmount {
   def this(value: String) = this(BigDecimal(value))
   def this(value: Int) = this(BigDecimal(s"$value"))
   def -(other: MoneyAmount): MoneyAmount = subtract(other)
   def subtract(other: MoneyAmount): MoneyAmount = MoneyAmount(value - other.value)
   def +(other: MoneyAmount): MoneyAmount = add(other)
   def add(other: MoneyAmount): MoneyAmount = MoneyAmount(value + other.value)
}

case class BankLoan(dateTakenOut: GameDate, bankLoanOffer: BankLoanOffer, loanOutstanding: MoneyAmount)

case class BankLoans(loans: Set[BankLoan]){
   val total = MoneyAmount(loans.map(_.loanOutstanding.value).sum)
}

case class BankLoanOffer(loanAmount: MoneyAmount, interest: NumberAmount, terms: Int) {
   lazy val interestRatePerMonth = (interest.value / BigDecimal("100")) / BigDecimal("12")
   lazy val monthlyInterest = interestRatePerMonth * loanAmount.value
   lazy val powered = (BigDecimal("1")+interestRatePerMonth).pow(-terms)
   lazy val repayments =
      MoneyAmount( (monthlyInterest / (1 - powered) ).setScale(0, BigDecimal.RoundingMode.HALF_UP) )
}

case class CompanyContext(companyName: String, balance: MoneyAmount, bankLoans: BankLoans)

case class GameContext(gameDate: GameDate, companyContext: CompanyContext)

case class JobDetails(
   companyName: String,
   freightType: String,
   pickup: String,
   destination: String,
   routeLength: Int,
   compensation: MoneyAmount,
   deadline: GameDate)

case class DeliveryExpenses(
   fuel: MoneyAmount,
   lodging: MoneyAmount
){
   lazy val total = MoneyAmount(Set(fuel, lodging).map(_.value).sum)
}

case class JobDelivered (
   jobDetails: JobDetails,
   timeTaken: Int,
   expenses: DeliveryExpenses
){
   val balance: MoneyAmount = jobDetails.compensation + expenses.total
}

case class TruckDetails(
   make: String,
   chassis: String,
   power: Int,
   steering: String,
   age: Int,
   mileage: Int,
   condition: String,
   hasCabin: Boolean)

case class TruckSaleDetails(truckDetails: TruckDetails, price: MoneyAmount)

case class GarageDetails(
   location: String,
   truckCapacity: Int
)

case class GarageSaleDetails(
   garageDetails: GarageDetails,
   price: MoneyAmount
)

case class Player(playerId: Option[Long], fullname: String){
   def this(fullname: String) = this(None, fullname)
   def save(implicit repositoryRegistry: RepositoryRegistry): Future[Player] =
    repositoryRegistry.playerRepository.get.save(this)
}

case class Company(companyId: Option[Long], companyName: String, ownerId: Option[Long]){
   def this(companyName: String, player: Player) = this(None, companyName, player.playerId)
   def save(implicit repositoryRegistry: RepositoryRegistry): Future[Company] =
      repositoryRegistry.companyRepository.get.save(this)
}
