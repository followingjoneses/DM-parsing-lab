/**
 * Created by jzl on 16/4/28.
 */

def test = "(() \\eq (A_{1} \\and A_{2}))"

parse = new parsing()

boolean answer = parse.equal_parentheses(test) && parse.is_well_defined(test)

assert answer