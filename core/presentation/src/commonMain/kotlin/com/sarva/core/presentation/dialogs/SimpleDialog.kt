package com.sarva.core.presentation.dialogs

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sarva.core.presentation.generated.resources.Res
import com.sarva.core.presentation.generated.resources.cancel
import com.sarva.core.presentation.generated.resources.confirm
import com.sarva.designsystem.theme.SarvaTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun SimpleDialog(
    title: String,
    description: String?= null,
    icon: ImageVector? = null,
    confirmText: String? = null,
    dismissText: String? = null,
    containerColor: Color = AlertDialogDefaults.containerColor,
    contentColor: Color = AlertDialogDefaults.iconContentColor,
    confirmLabelColor: Color = Color.Unspecified,
    dismissLabelColor: Color = Color.Unspecified,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = icon ?: Icons.Default.Info,
                modifier = Modifier.size(24.dp),
                tint = contentColor,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = contentColor
                )
            )
        },
        text = description?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = contentColor
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = confirmLabelColor)
            ) {
                Text(
                    text = confirmText ?: stringResource(Res.string.confirm),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = dismissLabelColor)
            ) {
                Text(
                    text = dismissText ?: stringResource(Res.string.cancel),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                    )
                )
            }
        },
        containerColor = containerColor,
        iconContentColor = contentColor,
        titleContentColor = contentColor,
        textContentColor = contentColor
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
fun PreviewSimpleDialogInfo() {
    SarvaTheme {
        SimpleDialog(
            title = "Update Available",
            description = "A new version of the application is ready to be installed. Would you like to update now?",
            onConfirm = {},
            onDismiss = {}
        )
    }
}