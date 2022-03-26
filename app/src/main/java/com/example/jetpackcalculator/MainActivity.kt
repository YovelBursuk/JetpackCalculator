package com.example.jetpackcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val operationsColumn = listOf("Delete", "÷", "x", "-", "+")
val numberColumns = listOf(
    listOf("7", "4", "1", "0"),
    listOf("8", "5", "2", "."),
    listOf("9", "6", "3", "=")
)

fun operatorFromChar(charOperator: Char):(Int, Int)->Int
{
    return when(charOperator)
    {
        '+'->{a,b->a+b}
        '-'->{a,b->a-b}
        '/'->{a,b->a/b}
        '*'->{a,b->a*b}
        else -> throw Exception("That's not a supported operator")
    }
}

fun performCalculation(input: String): String {
    val finalExpression = input.replace('x', '*').replace('÷', '/')
    val expressions = arrayOf('+', '-', '*', '/')
    var output = ""
    if (!expressions.any { finalExpression.contains(it) }) {
        return ""
    }
    if (expressions.any { finalExpression.endsWith(it) }) {
        return ""
    }

    // Simple math calculations
    expressions.forEach { expression ->
        if (finalExpression.contains(expression)) {
            val splitFinalExpression = finalExpression.split(expression)
            val leftArgument = splitFinalExpression[0].toInt()
            val rightArgument = splitFinalExpression[1].toInt()

            output = operatorFromChar(expression).invoke(leftArgument, rightArgument).toString()
            return@forEach
        }
    }
    return output
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var inputText by remember { mutableStateOf("") }
            var outputText by remember { mutableStateOf("0") }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                Box(modifier = Modifier.height(550.dp).offset(0.dp, 300.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(520.dp)
                            .background(Color.Gray)
                    ) {
                        Column(verticalArrangement = Arrangement.Bottom) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                numberColumns.forEach { numberColumn ->
                                    Column(modifier = Modifier.weight(3f)) {
                                        numberColumn.forEach { text ->
                                            IconButton(modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth(), onClick = {
                                                if (text == "=") {
                                                    outputText = performCalculation(inputText)
                                                } else {
                                                    if (inputText.length < 10) {
                                                        inputText += text
                                                    }

                                                    outputText = performCalculation(inputText)
                                                }
                                            }) {
                                                Box {
                                                    Text(
                                                        text = text,
                                                        style = TextStyle(
                                                            fontSize = 36.sp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Divider(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .fillMaxHeight(),
                                    color = Color(0xFFd3d3d3)
                                )
                                Column(modifier = Modifier.weight(1.3f)) {
                                    operationsColumn.forEach { operation ->
                                        if (operation == "Delete") {
                                            IconButton(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxWidth(),
                                                onClick = {
                                                    if (inputText.isNotEmpty()) {
                                                        inputText = inputText.substring(0, inputText.length - 1)
                                                    }
                                                        outputText = performCalculation(inputText)
                                                }) {
                                                Box {
                                                    Text(
                                                        text = "Del",
                                                        style = TextStyle(
                                                            color = Color.Black,
                                                            fontSize = 30.sp
                                                        )
                                                    )
                                                }
                                            }
                                        } else {
                                            IconButton(modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth(), onClick = {
                                                outputText = ""
                                                inputText += operation
                                            }) {
                                                Box {
                                                    Text(
                                                        text = operation,
                                                        style = TextStyle(
                                                            color = Color.Black,
                                                            fontSize = 36.sp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.width(30.dp))
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        ) {
                            TextField(
                                value = inputText,
                                onValueChange = { textFieldValue ->
                                    if (textFieldValue.length < 10) {
                                        inputText = textFieldValue
                                    }
                                },
                                textStyle = TextStyle(fontSize = 46.sp)
                            )
                            Text(
                                text = outputText,
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 36.sp,
                                ),
                                overflow = TextOverflow.Ellipsis,
                                softWrap = false,
                                maxLines = 1
                            )
                        }
                    }
                }

            }
        }
    }
}
