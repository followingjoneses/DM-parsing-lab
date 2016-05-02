/**
 * Created by jzl on 16/4/28.
 */
import groovy.swing.SwingBuilder
import javax.swing.*

class parsing {
    def proposition_letter_1 = ~/[A-Z]/
    def proposition_letter_2 = ~/[A-Z]_\{[0-9]+\}/

    boolean is_well_defined(String input) {
        if (proposition_letter_1.matcher(input).matches() || proposition_letter_2.matcher(input).matches())
            return true

        String left_proposition, right_proposition

        if (input[0] != '(')
            return false

        int index = 1

        /*
            unary connective
         */
        if (4 < input.length() && input[1..4] == "\\not") {
            index = 5
            int begin = index
            if (input[begin] == '(') {
                int left_parentheses = 0, right_parentheses = 0
                for (;index<input.length();index++) {
                    if (input[index] == '(')
                        left_parentheses++
                    else if (input[index] == ')') {
                        right_parentheses++
                        if (right_parentheses == left_parentheses) {
                            left_proposition = input[begin..index++]
                            break
                        }
                    }
                }
            } else if (input.length() > begin+1 && proposition_letter_1.matcher(input[begin]).matches() && input[begin+1] == ')') {
                left_proposition = input[begin]
                index = begin + 1
            } else {
                for (;index<input.length();index++) {
                    if (input[index] == '}' && proposition_letter_2.matcher(input[begin..index]).matches()) {
                        left_proposition = input[begin..index]
                        index++
                        break
                    }
                }
            }

            if (index != input.length() - 1)
                return false;
println(left_proposition)
            return is_well_defined(left_proposition)
        }

        /*
            binary connective
         */
        if (input[1] == '(') {
            int left_parentheses = 0, right_parentheses = 0
            for (;index<input.length();index++) {
                if (input[index] == '(')
                    left_parentheses++
                else if (input[index] == ')') {
                    right_parentheses++
                    if (right_parentheses == left_parentheses) {
                        left_proposition = input[1..index++]
                        break
                    }
                }
            }
        } else if (input.length() > 2 && proposition_letter_1.matcher(input[1]).matches() && input[2] == '\\') {
            left_proposition = input[1]
            index++
        } else {
            for (;index<input.length();index++) {
                if (input[index] == '}' && proposition_letter_2.matcher(input[1..index]).matches()) {
                    left_proposition = input[1..index++]
                    break
                }
            }
        }
println(left_proposition)

        if (input[index..index+3] == "\\and")
            index += 4
        else if (input[index..index+2] == "\\or" || input[index..index+2] == "\\eq")
            index += 3
        else if (input[index..index+5] == "\\imply")
            index += 6
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
        } else if (input.length() > begin+1 && proposition_letter_1.matcher(input[begin]).matches() && input[begin+1] == ')') {
            right_proposition = input[begin]
            index = begin + 1
        } else {
            for (;index<input.length();index++) {
                if (input[index] == '}' && proposition_letter_2.matcher(input[begin..index]).matches()) {
                    right_proposition = input[begin..index++]
                    break
                }
            }
        }

        if (index != input.length()-1)
            return false
println(right_proposition)

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
