package parse
/**
 * Created by jzl on 16/4/28.
 */

import tree.TextInBox

class parsing {
    def proposition_letter = ~/[A-Z]+(_\{\d+\})?/
    def proposition_symbol = ~/([\s]+\\and[\s]+|[\s]+\\or[\s]+|[\s]*\\not[\s]+|[\s]+\\imply[\s]+|[\s]+\\eq[\s]+|[\s]*\([\s]*|[\s]*\)[\s]*|[A-Z]+(_\{\d+\})?)+/

    boolean is_well_defined(TextInBox node) {
        String input = node.text

        if (proposition_letter.matcher(input).matches())
            return true

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
                            node.left = new TextInBox(input[begin..index++])
                            break
                        }
                    }
                }
            } else if (input.length() > begin+1 && proposition_letter.matcher(input[begin]).matches() && input[begin+1] == ')') {
                node.left = new TextInBox(input[begin])
                index = begin + 1
            } else {
                for (;index<input.length();index++) {
                    if (input[index] == '}' && proposition_letter.matcher(input[begin..index]).matches()) {
                        node.left = new TextInBox(input[begin..index++])
                        break
                    }
                }
            }

            if (index != input.length() - 1)
                return false;

            return is_well_defined(node.left)
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
                        node.left = new TextInBox(input[1..index++])
                        break
                    }
                }
            }
        } else if (input.length() > 2 && proposition_letter.matcher(input[1]).matches() && input[2] == '\\') {
            node.left = new TextInBox(input[1])
            index++
        } else {
            for (;index<input.length();index++) {
                if (input[index] == '}' && proposition_letter.matcher(input[1..index]).matches()) {
                    node.left = new TextInBox(input[1..index++])
                    break
                }
            }
        }

        if (input.length() > index+3 && input[index..index+3] == "\\and")
            index += 4
        else if (input.length() > index+2 && (input[index..index+2] == "\\or" || input[index..index+2] == "\\eq"))
            index += 3
        else if (input.length() > index+5 && input[index..index+5] == "\\imply")
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
                        node.right = new TextInBox(input[begin..index++])
                        break
                    }
                }
            }
        } else if (input.length() > begin+1 && proposition_letter.matcher(input[begin]).matches() && input[begin+1] == ')') {
            node.right = new TextInBox(input[begin])
            index = begin + 1
        } else {
            for (;index<input.length();index++) {
                if (input[index] == '}' && proposition_letter.matcher(input[begin..index]).matches()) {
                    node.right = new TextInBox(input[begin..index++])
                    break
                }
            }
        }

        if (index != input.length()-1)
            return false

        return is_well_defined(node.left) && is_well_defined(node.right);
    }

    boolean is_proposition_symbol(String input) {
        return input.matches(proposition_symbol)
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
