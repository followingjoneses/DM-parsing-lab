/**
 * Created by jzl on 16/4/28.
 */
import groovy.swing.SwingBuilder
import javax.swing.*

class parsing {
    def proposition_letter_1 = ~/[A-Z]/
    def proposition_letter_2 = ~/[A-Z]_\{[0-9]\}/
    def connectives = ["\\and", "\\or", "\\not", "\\imply", "\\eq"]

    boolean is_well_defined(String input) {
        if (proposition_letter_1.matcher(input).matches() || proposition_letter_2.matcher(input).matches())
            return true

        String left_proposition, right_proposition

        if (input[0] != '(')
            return false

        int index = 1

        if (input[1] == '(') {
            int left_parentheses = 0, right_parentheses = 0
            for (;index<input.length();index++) {
                if (input[index] == '(')
                    left_parentheses++
                else if (input[index] == ')') {
                    right_parentheses++
                    if (right_parentheses == left_parentheses) {
                        left_proposition = input[1..index]
                        index += 2
                        break
                    }
                }
            }
        } else if (5 < input.length() && proposition_letter_2.matcher(input[1..5]).matches()) {
            left_proposition = input[1..5]
            index = 5 + 2
        } else if (proposition_letter_1.matcher(input[1]).matches()) {
            left_proposition = input[1]
            index = 1 + 2
        } else
            return false

        //println(left_proposition)

        if (input[index..index+3] == "\\and")
            index += 5
        else if (input[index..index+2] == "\\or" || input[index..index+2] == "\\eq")
            index += 4
        else if (input[index..index+5] == "\\imply")
            index += 7
        else
            return false

        int begin = index
        if (input[begin] == '(') {
            int left_parentheses = 0, right_parentheses = 0
            for (;index<input.length();index++) {
                if (input[index] == '(')
                    left_parentheses++
                else if (input[index] == ')') {
                    right_parentheses++
                    if (right_parentheses == left_parentheses) {
                        right_proposition = input[begin..index++]
                        break
                    }
                }
            }
        } else if (begin + 4 < input.length() && proposition_letter_2.matcher(input[begin..begin+4]).matches()) {
            right_proposition = input[begin..begin+4]
            index = begin + 5
        } else if (proposition_letter_1.matcher(input[begin]).matches()) {
            right_proposition = input[begin]
            index = begin + 1
        } else
            return false

        //println(right_proposition)

        if (index != input.length()-1)
            return false


        return is_well_defined(left_proposition) && is_well_defined(right_proposition);
    }

    boolean equal_parentheses(String input) {
        int left_parentheses = 0, right_parentheses = 0

        for (int i in 0..input.length()-1) {
            if (input[i] == '(')
                left_parentheses++
            else if (input[i] == ')')
                right_parentheses++
        }

        return left_parentheses == right_parentheses
    }
}
