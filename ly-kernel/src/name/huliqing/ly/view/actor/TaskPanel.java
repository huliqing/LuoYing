/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.ly.Factory;
import name.huliqing.ly.constants.ResConstants;
import name.huliqing.ly.data.TaskData;
import name.huliqing.ly.layer.service.TaskService;
import name.huliqing.ly.view.SimpleCheckbox;
import name.huliqing.ly.view.SimpleRow;
import name.huliqing.ly.manager.ResourceManager;
import name.huliqing.ly.object.actor.Actor;
import name.huliqing.ly.object.task.Task;
import name.huliqing.ly.ui.Checkbox;
import name.huliqing.ly.ui.Checkbox.ChangeListener;
import name.huliqing.ly.ui.LinearLayout;
import name.huliqing.ly.ui.ListView;
import name.huliqing.ly.ui.Row;
import name.huliqing.ly.ui.Text;
import name.huliqing.ly.ui.UI;
import name.huliqing.ly.ui.UIFactory;
import name.huliqing.ly.ui.state.UIState;

/**
 * 任务面板，显示角色所有的任务信息
 * @author huliqing
 */
public class TaskPanel extends LinearLayout implements ActorPanel {
    private final TaskService taskService = Factory.get(TaskService.class);
    
    private Actor actor;
    
    private TaskList taskList;    // runningTasks
    
    private LinearLayout footerPanel;
    // 如果为true，则过滤掉已经完成的任务    
    private SimpleCheckbox filter;
    // 点击任务查看详情
    private Text tipsDetail;
    private Text tipsNoTasks;

    public TaskPanel(float width, float height) {
        super(width, height);
        
        taskList = new TaskList(width, height);
        
        filter = new SimpleCheckbox(ResourceManager.get(ResConstants.TASK_FILTER));
        filter.setFontColor(UIFactory.getUIConfig().getDesColor());
        filter.setFontSize(UIFactory.getUIConfig().getDesSize());
        filter.updateView();
        filter.resize();
        filter.addChangeListener(new ChangeListener() {
            @Override
            public void onChange(Checkbox ui) {
                if (actor != null && isVisible()) {
                    setPanelUpdate(actor);
                }
            }
        });
        
        tipsDetail = new Text(ResourceManager.get(ResConstants.TASK_TIPS));
        tipsDetail.setFontColor(UIFactory.getUIConfig().getDesColor());
        tipsDetail.setFontSize(UIFactory.getUIConfig().getDesSize());
        tipsDetail.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        tipsNoTasks = new Text(ResourceManager.get(ResConstants.TASK_NO_TASKS));
        tipsNoTasks.setFontColor(UIFactory.getUIConfig().getDesColor());
        tipsNoTasks.setFontSize(UIFactory.getUIConfig().getDesSize());
        tipsNoTasks.setVerticalAlignment(BitmapFont.VAlign.Center);
        
        footerPanel = new LinearLayout(width, UIFactory.getUIConfig().getListTitleHeight());
        footerPanel.setLayout(Layout.horizontal);
        footerPanel.setBackground(UIFactory.getUIConfig().getBackground(), true);
        footerPanel.setBackgroundColor(UIFactory.getUIConfig().getTitleBgColor(), true);
        footerPanel.addView(tipsDetail);
        footerPanel.addView(tipsNoTasks);
        footerPanel.addView(filter);
        addView(taskList);
        addView(footerPanel);
    }

    @Override
    protected void updateViewChildren() {
        super.updateViewChildren();
        float footerHeight = UIFactory.getUIConfig().getTitleHeight();
        float fw = footerHeight * 0.5f;
        taskList.setWidth(width);
        taskList.setHeight(height - footerHeight);
        
        footerPanel.setWidth(width);
        footerPanel.setHeight(footerHeight);
        
        tipsDetail.setHeight(footerHeight);
        tipsNoTasks.setHeight(footerHeight);
        filter.setHeight(fw);
        
        if (taskList.tasks.isEmpty()) {
            tipsDetail.setVisible(false);
            filter.setVisible(false);
            tipsNoTasks.setVisible(true);
        } else {
            tipsDetail.setVisible(true);
            filter.setVisible(true);
            tipsNoTasks.setVisible(false);
        }
    }

    @Override
    protected void updateViewLayout() {
        super.updateViewLayout(); 
        filter.setToCorner(Corner.RC);
    }

    @Override
    public void setPanelVisible(boolean visible) {
        this.setVisible(visible);
    }

    @Override
    public void setPanelUpdate(Actor actor) {
        this.actor = actor;
        taskList.tasks.clear();
        if (actor != null) {
            List<TaskData> tasks = taskService.getTaskDatas(actor);
            taskList.tasks.addAll(tasks);
            taskList.refreshPageData();
        }
    }
    
    public void setPageSize(int pageSize) {
        taskList.setPageSize(pageSize);
    }
    
    private class TaskList extends ListView<TaskData> {

        final List<TaskData> tasks = new ArrayList<TaskData>();
        
        public TaskList(float width, float height) {
            super(width, height);
        }

        @Override
        protected Row<TaskData> createEmptyRow() {
            return new TaskRow(this);
        }

        @Override
        public List<TaskData> getDatas() {
            return tasks;
        }

        @Override
        protected boolean filter(TaskData data) {
            if (filter.isChecked()) {
                return data.isCompletion();
            }
            return false; 
        }
        
    }
    
    private class TaskRow extends SimpleRow<TaskData> {

        private TaskData taskData;
        private Text text;
        private Text des;
        
        public TaskRow(ListView parentView) {
            super(parentView);
            setBackground(UIFactory.getUIConfig().getBackground(), true);
            setBackgroundColor(UIFactory.getUIConfig().getActiveColor(), true);
            setBackgroundVisible(false);
            
            text = new Text("");
            text.setVerticalAlignment(BitmapFont.VAlign.Center);
            
            des = new Text("");
            des.setVerticalAlignment(BitmapFont.VAlign.Center);
            des.setFontSize(UIFactory.getUIConfig().getDesSize());
            des.setFontColor(UIFactory.getUIConfig().getDesColor());
            
            addClickListener(new Listener() { 
                @Override
                public void onClick(UI view, boolean isPressed) {
                    if (isPressed || taskData == null) return;
                    Task task = taskService.getTask(actor, taskData.getId());
                    if (task == null)
                        return;
                    UIState.getInstance().addUI(task.getTaskDetail().getDisplay());
                }
            });
            
            addView(text);
            addView(des);
        }

        @Override
        protected void updateViewChildren() {
            super.updateViewChildren();
            text.setWidth(width);
            text.setHeight(height * 0.5f);
            des.setWidth(width);
            des.setHeight(height * 0.5f);
        }

        @Override
        public void displayRow(TaskData taskData) {
            this.taskData = taskData;
            des.setText(ResourceManager.getObjectDes(taskData.getId()));
            if (taskData.isCompletion()) {
                text.setText(ResourceManager.getObjectName(taskData.getId()) 
                        + "(" + ResourceManager.get(ResConstants.TASK_OVER) + ")");
                text.setFontColor(UIFactory.getUIConfig().getDesColor());
            } else {
                text.setText(ResourceManager.getObjectName(taskData.getId()));
                text.setFontColor(ColorRGBA.White);
            }
        }
        
    }
}
