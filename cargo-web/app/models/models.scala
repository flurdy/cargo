package models

import java.text.NumberFormat
import java.time.LocalDateTime


case class GameDate(year: Int, month: Int, week: Int)

case class MoneyAmount(value: BigDecimal){
   def this(value: String) = this(BigDecimal(value))
   lazy private val formatter = NumberFormat.getInstance
   lazy val formatted = formatter.format(value)
}

case class BankLoan(dateTakenOut: GameDate, loanAmount: MoneyAmount, loanOutstanding: MoneyAmount)

case class BankLoans(loans: Set[BankLoan]){
   val total = MoneyAmount(loans.map(_.loanOutstanding.value).sum)
}

case class CompanyContext(companyName: String, balance: MoneyAmount, bankLoans: BankLoans)

case class GameContext(gameDate: GameDate, companyContext: CompanyContext)
