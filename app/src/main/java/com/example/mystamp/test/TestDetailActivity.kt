@file:Suppress("DEPRECATION")

package com.example.mystamp.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mystamp.R
import com.example.mystamp.ui.theme.MyStampTheme

class TestDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStampTheme {
                Surface {
                    OverlappingCardsExample()
                }
            }
        }
    }

    @Composable
    fun OverlappingCardsExample() {
        var isExpanded by remember { mutableStateOf(false) }
        val cardHeight = 200.dp
        val cardOverlap = 30.dp

        Column(Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight - cardOverlap)
                    .background(Color.Gray)
                    .clickable {
                        isExpanded = !isExpanded
                    }
            ) {
                Spacer(modifier = Modifier.height(cardOverlap).background(Color.White))
            }

            if (isExpanded) {
                for (index in 0 until 3) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(cardHeight)
                            .background(
                                if (index % 2 == 0) Color.Green else Color.Blue
                            )
                            .padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = getCardImageResId(index)),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

//    @Preview
//    @Composable
//    fun PreviewOverlappingCards() {
//        MyStampTheme {
//            OverlappingCardsExample()
//        }
//    }

    private fun getCardImageResId(index: Int): Int {
        return when (index) {
            0 -> R.drawable.blank // Replace R.drawable.card_image_1 with your actual resource ID
            1 -> R.drawable.blank // Replace R.drawable.card_image_2 with your actual resource ID
            2 -> R.drawable.blank // Replace R.drawable.card_image_3 with your actual resource ID
            else -> R.drawable.blank
        }
    }
}