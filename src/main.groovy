
/**
 * Created by jzl on 16/4/28.
 */

import parse.parsing
import tree.*
import tree.swing.PaintTree

import javax.swing.JOptionPane

Scanner sc = new Scanner(new File("propositions.txt"))

while (sc.hasNextLine()) {
    String line = sc.nextLine()
    String trim_line = line.replaceAll(' ', '').replaceAll('\t', '')
    TextInBox root = new TextInBox(trim_line)
    parse = new parsing()
    boolean answer = parse.is_proposition_symbol(line) && parse.equal_parentheses(line) && parse.is_well_defined(root)
    println("proposition: " + line + "\t\twell-defined: " + answer)
    if (answer) {
        PaintTree.draw(root)
        JOptionPane.showMessageDialog(null, null, "next proposition", JOptionPane.PLAIN_MESSAGE)
    }
}