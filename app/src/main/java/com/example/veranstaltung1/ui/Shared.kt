package com.example.veranstaltung1.ui

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

/**
 * This composable fun will be called later and all this actions in the parameterr will be passed and executed
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericAppBar(
    // parameters
    title: String,
    onIconClick: (()->Unit)?, //  This will take a methode that will return a unit 
    icon:@Composable() (()-> Unit)?,
    iconState:MutableState<Boolean>
){
    // Action to be performed
    TopAppBar(title = { Text(text = title) /**/ },
        Modifier.background(MaterialTheme.colorScheme.primary), // backgroundColor from the color theme
        // my actions
        actions = {
            IconButton(onClick = {
            onIconClick?.invoke() // this involks the unit func in view that will be excuted here
              },
                content = {
                    if (iconState.value){
                        icon?.invoke()
                    }
                }

            )
        }
    )
}