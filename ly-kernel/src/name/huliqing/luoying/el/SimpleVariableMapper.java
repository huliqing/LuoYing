/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.luoying.el;

import java.util.HashMap;
import java.util.Map;

import javax.el.ValueExpression;
import javax.el.VariableMapper;

public final class SimpleVariableMapper extends VariableMapper {

  private final Map<String, ValueExpression> expressions = new HashMap<String, ValueExpression>();
  
  @Override
  public ValueExpression resolveVariable(String variable) {
    return expressions.get(variable);
  }

  @Override
  public ValueExpression setVariable(String variable, ValueExpression expression) {
    return expressions.put(variable, expression);
  }

}
