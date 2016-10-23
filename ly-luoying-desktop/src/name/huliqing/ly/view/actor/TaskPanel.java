/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.huliqing.ly.view.actor;

import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import java.util.List;
import name.huliqing.luoying.Factory;
import name.huliqing.luoying.constants.ResConstants;
import name.huliqing.luoying.data.TaskData;
import name.huliqing.luoying.layer.service.TaskService;
import name.huliqing.ly.view.SimpleCheckbox;
import name.huliqing.ly.view.SimpleRow;
import name.huliqing.luoying.manager.ResourceManager;
import name.huliqing.luoying.object.entity.Entity;
import name.huliqing.luoying.object.task.Task;
import name.huliqing.luoying.ui.Checkbox;
import name.huliqing.luoying.ui.Checkbox.ChangeListener;
import name.huliqing.luoying.ui.LinearLayout;
import name.huliqing.luoying.ui.ListView;
import name.huliqing.luoying.ui.Row;
import name.huliqing.luoying.ui.Text;
import name.huliqing.luoying.ui.UI;
import name.huliqing.luoying.ui.UIFactory;

/**
 * 任务面板，显示角色所有的任务信息
 * @author huliqing
 */
public class TaskPanel extends LinearLayout implements ActorPanel {
    private final TaskService taskService = Factory.get(TaskService.class);
    
    private Entity actor;
    
    private final TaskList taskList;    // runningTasks
    
    private final LinearLayout footerPanel;
    // 如果为true，则过滤掉已经完成的任务    
    private final SimpleCheckbox filter;
    // 点击任务查看详情
    private final Text tipsDetail;
    private final Text tipsNoTasks;

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
    public void setPanelUpdate(Entity actor) {
        this.actor = actor;
        taskList.tasks.clear();
        if (actor != null) {
            List<Task> tasks = taskService.getTasks(actor);
            if  (tasks != null && !tasks.isEmpty()) {
                for (Task t : tasks) {
                    taskList.tasks.add(t.getData());
                }
            }
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
//                    UIState.getInstance().addUI(task.getTaskDetail().getDisplay());
                    throw new UnsupportedOperationException("Unsupported");
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
