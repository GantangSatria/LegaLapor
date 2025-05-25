package com.example.legalapor.splashscreen

import androidx.annotation.DrawableRes
import com.example.legalapor.R

sealed class OnBoardingPage(
    @DrawableRes val background: Int,
    val title: String,
    val description: String
) {
    object First : OnBoardingPage(
        background = R.drawable.bg_first,
        title = "Konsultasi Hukum",
        description = "Dapatkan bantuan dan konsultasi hukum langsung dari para ahli!\n"
    )
    object Second : OnBoardingPage(
        background = R.drawable.bg_second,
        title = "Laporkan Kasus",
        description = "Ingin melaporkan kasus? Laporkan dengan mudah dan terarah di LegaLapor.\n"
    )
    object Third : OnBoardingPage(
        background = R.drawable.bg_third,
        title = "Lacak Kasus Anda",
        description = "Pantau perkembangan kasus Anda secara real-time di LegaLapor."
    )

    object Fourth : OnBoardingPage(
        background = R.drawable.bg_fourth,
        title = "Forum dan Komunitas Hukum",
        description = "Bergabunglah dengan komunitas kami! Diskusikan dan berbagi pengalaman seputar isu hukum!"
    )

    object Fifth : OnBoardingPage(
        background = R.drawable.bg_fifth,
        title = "LegaLapor",
        description = "Terhubung dengan pengacara, akses edukasi hukum, dan lacak perkembangan kasus Anda-semuanya di satu tempat."
    )
}

