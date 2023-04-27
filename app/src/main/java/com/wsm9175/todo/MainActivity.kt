package com.wsm9175.todo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
                    TopLevel()
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TopLevel() {
    val (text, setText) = remember { mutableStateOf("") }
    val toDoList = remember { mutableStateListOf<ToDoData>() }

    val onSubmit: (String) -> Unit = { text ->
        val key = (toDoList.lastOrNull()?.key ?: 0) + 1
        toDoList.add(ToDoData(key, text))
        setText("")
    }

    val onToggle: (Int, Boolean) -> Unit = { key, checked ->
        val i = toDoList.indexOfFirst { it.key == key }
        toDoList[i] = toDoList[i].copy(done = checked)
    }

    val onEdit: (Int, String) -> Unit = { key, text ->
        val i = toDoList.indexOfFirst { it.key == key }
        toDoList[i] = toDoList[i].copy(text = text)
    }

    val onDelete: (Int) -> Unit = { key ->
        val i = toDoList.indexOfFirst { it.key == key }
        toDoList.removeAt(i)
    }

    Scaffold() {
        Column() {
            ToDoInput(
                text = text,
                onTextChange = setText,
                onSubmit = onSubmit
            )

            LazyColumn {
                items(toDoList, key = { it.key }) { toDoData ->
                    ToDo(
                        toDoData = toDoData,
                        onToggle = onToggle,
                        onDelete = onDelete,
                        onEdit = onEdit
                    )
                }
            }
        }
    }
}

@Composable
fun ToDo(
    toDoData: ToDoData,
    onEdit: (key: Int, text: String) -> Unit = { _, _ -> },
    onToggle: (key: Int, checked: Boolean) -> Unit = { _, _ -> },
    onDelete: (key: Int) -> Unit = {}
) {
    var isEditing by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(4.dp),
        elevation = 8.dp
    ) {
        Crossfade(targetState = isEditing) {
            when (it) {
                false -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = toDoData.text,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "완료"
                        )
                        Checkbox(
                            checked = toDoData.done,
                            onCheckedChange = { checked ->
                                onToggle(toDoData.key, checked)
                            }
                        )
                        Button(onClick = {
                            isEditing = true
                        }) {
                            Text(text = "수정")
                        }
                        Spacer(modifier = Modifier.size(4.dp))
                        Button(onClick = {
                            onDelete(toDoData.key)
                        }) {
                            Text(text = "삭제")
                        }
                    }
                }
                true -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var (newText, setNewText) = remember { mutableStateOf(toDoData.text) }
                        OutlinedTextField(
                            value = newText,
                            onValueChange = setNewText,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Button(onClick = {
                            onEdit(toDoData.key, newText)
                            isEditing = false
                        }) {
                            Text(text = "완료")
                        }
                    }
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
) {
    Row(
        Modifier.padding(8.dp)
    ) {
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
fun ToDoInputPreview() {
    TODOTheme {
        ToDoInput("테스트", {}, {})
    }
}

@Preview(showBackground = true)
@Composable
fun ToDoDataPreview() {
    TODOTheme {
        ToDo(toDoData = ToDoData(1, "nice", true))
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TODOTheme {
        TopLevel()
    }
}

data class ToDoData(
    val key: Int,
    val text: String,
    val done: Boolean = false
)