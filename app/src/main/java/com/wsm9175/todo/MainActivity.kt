package com.wsm9175.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wsm9175.todo.ui.theme.TODOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODOTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }
            }
        }
    }
}

@Composable
fun ToDoInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSubmit: (String) -> Unit,
){
    Row(
        Modifier.padding(8.dp)
    ){
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.size(8.dp))

        Button(
            onClick = {
                onSubmit(text)
            }
        ) {
            Text(text = "입력")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ToDoInputPreview(){
    TODOTheme {
        ToDoInput("테스트", {}, {})
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TODOTheme {
    }
}