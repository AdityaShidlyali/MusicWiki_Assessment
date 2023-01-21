package com.adityamshidlyali.musicwiki.utility

import org.jsoup.Jsoup
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object Utility {

    private val suffixes: NavigableMap<Long, String> = TreeMap()

    /**
     * format numbers in the form of standard readable format
     */
    fun formatNumbers(value: Long): String {
        suffixes[1_000L] = "k"
        suffixes[1_000_000L] = "M"
        suffixes[1_000_000_000L] = "G"
        suffixes[1_000_000_000_000L] = "T"
        suffixes[1_000_000_000_000_000L] = "P"
        suffixes[1_000_000_000_000_000_000L] = "E"

        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatNumbers(Long.MIN_VALUE + 1)
        if (value < 0) return "-" + formatNumbers(-value)
        if (value < 1000) return java.lang.Long.toString(value) //deal with easy case

        val (divideBy, suffix) = suffixes.floorEntry(value)!!
        val truncated = value / (divideBy / 10) //the number part of the output times 10
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
    }

    /**
     * remove any HTML tags in the string
     */
    fun removeUrlFromString(string: String): String {
        val urlPattern =
            "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%<>/;$()~_?\\+-=\\\\\\.&]*)"
        val p: Pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE)
        val m: Matcher = p.matcher(string)
        var i = 0
        var newString = string
        while (m.find()) {
            newString = m.group(i)?.let { string.replace(it, "").trim() }.toString()
            i++
        }

        newString = Jsoup.parse(newString).text()

        return newString
    }
}