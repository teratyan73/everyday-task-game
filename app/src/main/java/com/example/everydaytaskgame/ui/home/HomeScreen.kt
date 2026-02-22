package com.example.everydaytaskgame.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.everydaytaskgame.data.db.entity.TaskEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddTask: () -> Unit,
    onNavigateToEditTask: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "習慣化タスク",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTask,
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "タスクを追加",
                    tint = Color.Black
                )
            }
        }
    ) { innerPadding ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // ---- キャラクターセクション ----
            item {
                CharacterSection(
                    emoji = uiState.evolutionEmoji,
                    stageName = uiState.evolutionName,
                    totalPoints = uiState.totalPoints,
                    pointsToNext = uiState.pointsToNext
                )
            }

            // ---- ストリーク・ポイント表示 ----
            item {
                StatsSection(
                    globalStreak = uiState.globalStreak,
                    totalPoints = uiState.totalPoints
                )
            }

            // ---- タスクリストヘッダー ----
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "今日のタスク",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val completed = uiState.tasks.count { it.isCompletedToday }
                    val total = uiState.tasks.size
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (completed == total && total > 0)
                            MaterialTheme.colorScheme.secondary
                        else
                            MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "$completed / $total",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ---- タスクリスト ----
            if (uiState.tasks.isEmpty()) {
                item {
                    EmptyTasksHint()
                }
            } else {
                items(uiState.tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTask(task.id) },
                        onEdit = { onNavigateToEditTask(task.id) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

// ---- キャラクター表示 ----

@Composable
private fun CharacterSection(
    emoji: String,
    stageName: String,
    totalPoints: Int,
    pointsToNext: Int?
) {
    val stageColor = stageBackgroundColor(stageName)
    val animatedColor by animateColorAsState(
        targetValue = stageColor,
        animationSpec = tween(durationMillis = 600),
        label = "characterColor"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // キャラクタープレースホルダー（後で画像に差し替え）
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(animatedColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 56.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stageName,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        if (pointsToNext != null) {
            Text(
                text = "次の進化まで あと ${pointsToNext}pt",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            Text(
                text = "最高段階に達しました！",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun stageBackgroundColor(stageName: String): Color {
    return when (stageName) {
        "タマゴ"     -> Color(0xFFE0E0E0)
        "ひよこ"     -> Color(0xFFB8E6B8)
        "そだちかけ" -> Color(0xFF74C69D)
        "おとな"     -> Color(0xFF2D6A4F)
        "でんせつ"   -> Color(0xFFFFD700)
        else         -> Color(0xFFE0E0E0)
    }
}

// ---- 統計表示 ----

@Composable
private fun StatsSection(globalStreak: Int, totalPoints: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            label = "全体ストリーク",
            value = "$globalStreak 日",
            emoji = "🔥"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            label = "総合ポイント",
            value = "$totalPoints pt",
            emoji = "⭐"
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    emoji: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

// ---- タスクアイテム ----

@Composable
private fun TaskItem(
    task: TaskEntity,
    onToggle: () -> Unit,
    onEdit: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (task.isCompletedToday)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 300),
        label = "taskBg"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompletedToday,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (task.isCompletedToday) TextDecoration.LineThrough else null,
                    color = if (task.isCompletedToday)
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                if (task.streak > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🔥",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${task.streak} 日連続",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "編集",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ---- タスクなし表示 ----

@Composable
private fun EmptyTasksHint() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "📋", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "タスクがまだありません",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
            Text(
                text = "右下の ＋ ボタンで追加しましょう",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
        }
    }
}
