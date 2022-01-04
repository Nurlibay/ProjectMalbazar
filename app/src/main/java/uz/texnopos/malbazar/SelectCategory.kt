package uz.texnopos.malbazar

class SelectCategory {
    fun selectCategory(category_id: Int): String {
        return when (category_id) {
            1 -> "Ӄарамал"
            2 -> "Түйе"
            3 -> "Жылӄы"
            4 -> "Ешки"
            5 -> "Ӄой"
            6 -> "Мал азығы"
            7 -> "Таўиқ"
            else -> ""
        }
    }
}