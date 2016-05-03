package parse
/**
 * Created by jzl on 16/4/28.
 */

import tree.*
import tree.swing.PaintTree

def test = "((\\not ((\\not A_{1}) \\and (A_{5} \\imply B))) \\eq C)"

test = test.replace(' ', '').replace('\t', '')

TextInBox root = new TextInBox(test)

parse = new parsing()

boolean answer = parse.equal_parentheses(test) && parse.is_well_defined(root)

println(answer)

if (answer) {
    PaintTree.draw(root)
}