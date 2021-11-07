package com.hyosakura.analyzer.grammar.detector

import com.hyosakura.analyzer.grammar.Grammar

/**
 * @author LovesAsuna
 **/
interface Detector {
    /**
     * 根据定义好的检测器检测文法规则中的问题，返回此文法
     * @param grammar 文法
     * @return 此文法
     */
    fun detect(grammar: Grammar): Grammar
}