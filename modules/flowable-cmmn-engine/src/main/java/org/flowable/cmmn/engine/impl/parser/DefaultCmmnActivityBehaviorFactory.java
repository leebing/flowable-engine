/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.cmmn.engine.impl.parser;

import org.apache.commons.lang3.StringUtils;
import org.flowable.cmmn.engine.impl.behavior.impl.CaseTaskActivityBehavior;
import org.flowable.cmmn.engine.impl.behavior.impl.HumanTaskActivityBehavior;
import org.flowable.cmmn.engine.impl.behavior.impl.MilestoneActivityBehavior;
import org.flowable.cmmn.engine.impl.behavior.impl.PlanItemDelegateExpressionActivityBehavior;
import org.flowable.cmmn.engine.impl.behavior.impl.PlanItemExpressionActivityBehavior;
import org.flowable.cmmn.engine.impl.behavior.impl.ProcessTaskActivityBehavior;
import org.flowable.cmmn.engine.impl.behavior.impl.StageActivityBehavior;
import org.flowable.cmmn.engine.impl.behavior.impl.TaskActivityBehavior;
import org.flowable.cmmn.engine.impl.behavior.impl.TimerEventListenerActivityBehaviour;
import org.flowable.cmmn.engine.impl.delegate.CmmnClassDelegate;
import org.flowable.cmmn.engine.impl.delegate.CmmnClassDelegateFactory;
import org.flowable.cmmn.model.CaseTask;
import org.flowable.cmmn.model.HumanTask;
import org.flowable.cmmn.model.Milestone;
import org.flowable.cmmn.model.PlanItem;
import org.flowable.cmmn.model.ProcessTask;
import org.flowable.cmmn.model.ServiceTask;
import org.flowable.cmmn.model.Stage;
import org.flowable.cmmn.model.Task;
import org.flowable.cmmn.model.TimerEventListener;
import org.flowable.engine.common.api.delegate.Expression;
import org.flowable.engine.common.impl.el.ExpressionManager;

/**
 * @author Joram Barrez
 */
public class DefaultCmmnActivityBehaviorFactory implements CmmnActivityBehaviorFactory {
    
    protected CmmnClassDelegateFactory classDelegateFactory;
    protected ExpressionManager expressionManager;

    @Override
    public StageActivityBehavior createStageActivityBehavoir(PlanItem planItem, Stage stage) {
        return new StageActivityBehavior(stage);
    }
    
    @Override
    public MilestoneActivityBehavior createMilestoneActivityBehavior(PlanItem planItem, Milestone milestone) {
        String name = null;
        if (!StringUtils.isEmpty(planItem.getName())) {
            name = planItem.getName();
        } else if (StringUtils.isNotEmpty(milestone.getName())) {
            name = milestone.getName();
        }
        return new MilestoneActivityBehavior(expressionManager.createExpression(name));
    }
    
    @Override
    public TaskActivityBehavior createTaskActivityBehavior(PlanItem planItem, Task task) {
        return new TaskActivityBehavior(task);
    }
    
    @Override
    public HumanTaskActivityBehavior createHumanTaskActivityBehavior(PlanItem planItem, HumanTask humanTask) {
        return new HumanTaskActivityBehavior(humanTask);
    }
    
    @Override
    public CaseTaskActivityBehavior createCaseTaskActivityBehavior(PlanItem planItem, CaseTask caseTask) {
        return new CaseTaskActivityBehavior(expressionManager.createExpression(caseTask.getCaseRef()), caseTask);
    }
    
    @Override
    public ProcessTaskActivityBehavior createProcessTaskActivityBehavior(PlanItem planItem, ProcessTask processTask) {
        Expression processRefExpression = null;
        if (StringUtils.isNotEmpty(processTask.getProcessRefExpression())) {
            processRefExpression = expressionManager.createExpression(processTask.getProcessRefExpression());
        }
        return new ProcessTaskActivityBehavior(processTask.getProcess(), processRefExpression, processTask);
    }
    
    @Override
    public CmmnClassDelegate createCmmnClassDelegate(PlanItem planItem, ServiceTask task) {
        return classDelegateFactory.create(task.getImplementation(), task.getFieldExtensions());
    }

    @Override
    public PlanItemExpressionActivityBehavior createPlanItemExpressionActivityBehavior(PlanItem planItem, ServiceTask task) {
        return new PlanItemExpressionActivityBehavior(task.getImplementation(), task.getResultVariableName());
    }

    @Override
    public PlanItemDelegateExpressionActivityBehavior createPlanItemDelegateExpressionActivityBehavior(PlanItem planItem, ServiceTask task) {
        return new PlanItemDelegateExpressionActivityBehavior(task.getImplementation(), task.getFieldExtensions());
    }
    
    @Override
    public TimerEventListenerActivityBehaviour createTimerEventListenerActivityBehavior(PlanItem planItem, TimerEventListener timerEventListener) {
        return new TimerEventListenerActivityBehaviour(timerEventListener);
    }

    public CmmnClassDelegateFactory getClassDelegateFactory() {
        return classDelegateFactory;
    }

    public void setClassDelegateFactory(CmmnClassDelegateFactory classDelegateFactory) {
        this.classDelegateFactory = classDelegateFactory;
    }

    public ExpressionManager getExpressionManager() {
        return expressionManager;
    }

    public void setExpressionManager(ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }
    
}
