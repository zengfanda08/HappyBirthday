package com.fanda.happybirthday.words.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.fanda.happybirthday.words.model.MAX_NO_OF_WORDS
import com.fanda.happybirthday.words.model.SCORE_INCREASE
import com.fanda.happybirthday.words.model.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


/*
*
* 只能在 GameViewModel 中对 _uiState 进行访问和修改。界面可以使用只读属性 uiState 读取其值
*
* 可变属性的更改只能通过事件驱动或通过 ViewModel 类内部进行修改，禁止外部随意修改，可增加统一方法暴露去外界修改
* 外界只能获取其只读属性 uiState 进行状态更新，达到单向数据流的逻辑
* */
class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())

    // asStateFlow() 会使此可变状态流成为只读状态流
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currentWord: String

    // 存储已使用的单词
    private var usedWords: MutableSet<String> = mutableSetOf()

    // 用户猜测的单词 [要使用 mutableStateOf ，不然无法触发重组]，公开该方法，暴露给外部使用，但是不允许修改
    var userGuess by mutableStateOf("")
        private set

    init {
        resetGame()
    }

    // 从列表中随机选择一个单词并打乱单词的字母顺序
    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        // 已使用的单词数量最多10个，远小于 allWords 的数量，不会出现死循环问题
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    // 打乱当前单词的字母顺序
    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // 打乱
        tempWord.shuffle()
        // 确保打乱的单词和原始单词不同
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    // 重置游戏
    fun resetGame() {
        // 清空已使用的单词
        usedWords.clear()
        // 获取新的单词
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    // 更新用户猜测的单词
    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
        clearUserGuessErrorState()
    }

    // 检查用户猜测的答案
    fun checkUserGuess() {
        // 忽略大小写
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updateScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updateScore)
        } else {
            _uiState.update { currentState ->
                // 通过 copy 方法创建新的状态对象，同时保持其余属性值不变
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        // 清零用户猜测
        updateUserGuess("")
    }

    private fun updateGameState(score: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                // 通过 copy 方法创建新的状态对象，同时保持其余属性值不变
                currentState.copy(
                    score = score, isGuessedWordWrong = false, isGameOver = true
                )
            }
        } else {
            // inc 方法加1
            _uiState.update { currentState ->
                // 通过 copy 方法创建新的状态对象，同时保持其余属性值不变
                currentState.copy(
                    score = score, isGuessedWordWrong = false, currentScrambledWord = pickRandomWordAndShuffle(), currentWordCount = currentState.currentWordCount.inc()
                )
            }
        }
    }

    // 清空错误状态
    private fun clearUserGuessErrorState() {
        if (_uiState.value.isGuessedWordWrong && userGuess.isNotEmpty()) {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = false)
            }
        }
    }

    // 跳过单词
    fun skipWord() {
        // 让单词数量加1，分数不变
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }


}