/**
 * Created by jzl on 16/4/28.
 */

def test = "(((C \\and D) \\or (\\not B))   \\eq (A_{14324} \\and A_{2}))"

test = test.replace(' ', '').replace('\t', '')

println(test)

parse = new parsing()

boolean answer = parse.equal_parentheses(test) && parse.is_well_defined(test)

assert answer