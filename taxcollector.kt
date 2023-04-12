import java.text.NumberFormat
import java.util.*

// Formats the given double into the PH Currency format. "18000.00" becomes "₱18,000.00"
fun format(value: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
    return formatter.format(value)
}

// Takes a PH currency format string and converts it to a double. "₱18,000.00" becomes "18000.00"
fun deformat(value: String): Double {
    val cleanValue = value.replace(Regex("[^\\d.]"), "")
    return cleanValue.toDoubleOrNull() ?: 0.0
}

// Takes the taxable income, and uses a when (basically a switch statement with direct assignment) to
// calculate the income tax based on a person's Taxable Income

fun computeMonthlyTax(taxableIncome: Double) : Double {

    val incomeTax = when (taxableIncome) {
        in Double.MIN_VALUE..20833.0 -> 0.00
        in 20833.0..33332.99 -> (taxableIncome - 20833) * 0.20
        in 33333.0..66666.99 -> ((taxableIncome - 33333) * 0.25) + 2500.00
        in 66667.0..166666.99 -> ((taxableIncome - 66667) * 0.30) + 10833.33
        in 166667.0..666666.99 -> ((taxableIncome - 166667) * 0.32) + 40833.33
        else -> ((taxableIncome - 666667) * 0.35) + 200833.33
    }

    return incomeTax
}

// Takes the monthly income, and uses a "when" statement to find the SSS contribution based on the monthlyIncome.
fun computeSSS(monthlyIncome: Double): Double {

    val sss = when (monthlyIncome){
        in Double.MIN_VALUE..3249.99 -> 135.00
        in 4250.0..24749.99 -> kotlin.math.ceil((monthlyIncome - 3250)/500.00) * 22.50 + 135.00
        else -> 1125.00
    }

    return sss
}

// Takes the monthly income, and uses a "when" statement to find the PhilHealth contribution based on the monthlyIncome.
fun computePH(monthlyIncome: Double): Double {

    val ph = when (monthlyIncome) {
        in Double.MIN_VALUE..10000.00 -> 200.00
        in 10000.01..79999.99 -> monthlyIncome * 0.04 / 2
        else -> 1600.00
    }

    return ph
}

// Takes the monthly income, and uses a "when" statement to find the Pag-ibig contribution based on the monthlyIncome.
fun computePagIbig(monthlyIncome: Double): Double {

    val pagibig = when (monthlyIncome){
        in Double.MIN_VALUE..1500.00 -> monthlyIncome * 0.01
        in 1500.01..4999.99 -> monthlyIncome * 0.02
        else -> 100.00
    }

    return pagibig
}

fun main() {
    
    val monthlyIncome: Double
    val sssContributions: Double
    val phContributions: Double
    val pagibigContributions: Double
    val totalContributions: Double
    val taxableIncome: Double
    val incomeTax: Double


    print("Enter your Monthly Income: ")
    
    //This allows the user to input either in formal peso format "₱18,000" or direct numbers assumed to be in peso such as "18000"
    monthlyIncome = deformat(readln())

    // Calculations
    sssContributions = deformat(format(computeSSS(monthlyIncome)))
    phContributions = deformat(format(computePH(monthlyIncome)))
    pagibigContributions = deformat(format(computePagIbig(monthlyIncome)))
    totalContributions = deformat(format(sssContributions + phContributions + pagibigContributions))
    taxableIncome = deformat(format(monthlyIncome - totalContributions))
    incomeTax = deformat(format(computeMonthlyTax(taxableIncome)))

    // format then deformat is there to round it off to two decimal places, and converts it directly into double

    // Printing
    println("SSS Contributions: " + format(sssContributions))
    println("PhilHealth Contributions: " + format(phContributions))
    println("Pag-ibig Contributions: " + format(pagibigContributions))
    println("Total Contributions: " + format(totalContributions))
    println("Pay after Deductions: " + format(taxableIncome))
    println("Income Tax: " + format(incomeTax))
    println("Net Pay After Tax: " + format(monthlyIncome - incomeTax - totalContributions))
}