package uz.texnopos.malbazar.core

class SelectCity {
    fun selectCity(cityId: Int): String {
        return when (cityId) {
            1 -> "Shımbay"
            2 -> "Nókis"
            3 -> "Qońırat"
            4 -> "Xojeli"
            5 -> "Taxtakópir"
            6 -> "Moynaq"
            7 -> "Nókıs rayon"
            8 -> "Kegeyli"
            9 -> "Qanlıkól"
            10 -> "Shomanay"
            11 -> "Amudarya"
            12 -> "Beruniy"
            13 -> "Tórtkól"
            14 -> "Elliqqala"
            15 -> "Bozataw"
            16 -> "Qaraózek"
            17 -> "Taqiatas"
            else -> ""
        }
    }
}