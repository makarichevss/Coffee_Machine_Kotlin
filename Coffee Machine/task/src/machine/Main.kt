package machine

fun main() {
    CoffeeMachine.setInitialResources(400, 540, 120, 9, 550).also {
        CoffeeMachine.mainMenu() }
}

class CoffeeMachine {
    private enum class Coffee(
        water: Int, milk: Int, coffeeBeans: Int, cups:
        Int, money: Int
    ) {
        ESPRESSO(250, 0, 16, 1, -4),
        LATTE(350, 75, 20, 1, -7),
        CAPPUCCINO(200, 100, 12, 1, -6);

        val requiredResources = mutableListOf(water, milk, coffeeBeans, cups,
            money)
    }

    private enum class Resources(val resourceName: String, var remainder:
    Int, val measure: String) {
        WATER("water", 0, " ml"),
        MILK("milk", 0, " ml"),
        COFFEE_BEANS("coffee beans", 0, " grams"),
        CUPS("disposable cups", 0, ""),
        MONEY("money", 0, "");
    }
    
    companion object {
        fun setInitialResources(water: Int, milk: Int, coffeeBeans: Int, cups: Int, money: Int) {
            val initialResources = mutableListOf(water, milk, coffeeBeans, cups, money)
            for (resource in Resources.values()) {
                resource.remainder = initialResources[resource.ordinal]
            }
        }

        fun mainMenu() {
            print("Write action (buy, fill, take, remaining, exit): ")
            when (readln()) {
                "buy" -> selectDrink()
                "fill" -> println().also { fillResources() }
                "take" -> withdrawMoney()
                "remaining" -> printRemainder()
                "exit" -> return
            }
            println().also { mainMenu() }
        }

        private fun selectDrink() {
            print("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
            when (val option = readln()) {
                "back" -> return
                else -> for (coffee in Coffee.values()) {
                    if (option == (coffee.ordinal + 1).toString())
                        makeCoffee(coffee)
                }
            }
        }

        private fun makeCoffee(coffee: Coffee) {
            for (resource in Resources.values()) {
                if (resource.remainder < coffee.requiredResources[resource
                        .ordinal]) {
                    println("Sorry, not enough ${resource.resourceName}!")
                        .also { return }
                }
                resource.remainder -= coffee.requiredResources[resource.ordinal]
            }
            println("I have enough resources, making you a coffee!")
        }

        private fun fillResources() {
            for (resource in Resources.values()) {
                if (resource.resourceName.contains("money")) continue
                print("Write how many${resource.measure} of ${resource
                    .resourceName} do you want to add: ")
                resource.remainder += readln().toInt()
            }
        }

        private fun withdrawMoney() {
            println("\nI gave you $${Resources.MONEY.remainder}").also {
                Resources.MONEY.remainder = 0 }
        }

        private fun printRemainder() {
            println("\nThe coffee machine has:")
            for (resource in Resources.values()) {
                print(if (resource.resourceName == "money") "$" else "")
                println("${resource.remainder}${resource.measure} of ${resource.resourceName}")
            }
        }
    }
}