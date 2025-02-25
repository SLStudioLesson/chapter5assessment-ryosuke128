package com.taskapp.logic;

import java.util.*;
import java.time.LocalDate;

import com.taskapp.dataaccess.LogDataAccess;
import com.taskapp.dataaccess.TaskDataAccess;
import com.taskapp.dataaccess.UserDataAccess;
import com.taskapp.exception.AppException;
import com.taskapp.model.*;

public class TaskLogic {
    private final TaskDataAccess taskDataAccess;
    private final LogDataAccess logDataAccess;
    private final UserDataAccess userDataAccess;


    public TaskLogic() {
        taskDataAccess = new TaskDataAccess();
        logDataAccess = new LogDataAccess();
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param taskDataAccess
     * @param logDataAccess
     * @param userDataAccess
     */
    public TaskLogic(TaskDataAccess taskDataAccess, LogDataAccess logDataAccess, UserDataAccess userDataAccess) {
        this.taskDataAccess = taskDataAccess;
        this.logDataAccess = logDataAccess;
        this.userDataAccess = userDataAccess;
    }

    /**
     * 全てのタスクを表示します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findAll()
     * @param loginUser ログインユーザー
     */
    public void showAll(User loginUser) {
        /*
         * 1．taskDataAccessのfinedAllからリストを受け取る
         * 2．taskクラスのUserがLoginUser名と一致した場合は「あなた」、そうでない場合はUserName
         * 3．statusの数字によって値を変更
         * 4．上記情報を出力
         */

        List<Task> tasks = taskDataAccess.findAll();
        tasks.forEach(task -> {
            int status = task.getStatus();
            String statusMsg = "未着手";
            if (status == 1) statusMsg = "着手中";
            if (status == 2) statusMsg = "完了";

            String userName = task.getRepUser().getName();
            if (userName.equals(loginUser.getName())) {
                userName = "あなた";
            }

            System.out.println(task.getCode() + ". タスク名：" + task.getName() + ", 担当者名：" + 
                        userName + "が担当しています, ステータス：" + statusMsg);
        });
    }

    /**
     * 新しいタスクを保存します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#save(com.taskapp.model.Task)
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.model.Log)
     * @param code タスクコード
     * @param name タスク名
     * @param repUserCode 担当ユーザーコード
     * @param loginUser ログインユーザー
     * @throws AppException ユーザーコードが存在しない場合にスローされます
     */
    public void save(int code, String name, int repUserCode,
                    User loginUser) throws AppException {
    /*
     * 1．引数で受け取った情報からTaskクラス・Logクラスをインスタンス化
     * 2．入力されたユーザーコードが存在しない場合は例外をスロー
     * 3．インスタンスをもとにcsvへ保存
     */
        User user = userDataAccess.findByCode(repUserCode);
        if (user == null) throw new AppException("存在するユーザーコードを入力してください");

        Log log = new Log(code, loginUser.getCode(), 0, LocalDate.now());
        Task task = new Task(code, name, 0, user);

        logDataAccess.save(log);
        taskDataAccess.save(task);
    }

    /**
     * タスクのステータスを変更します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#update(com.taskapp.model.Task)
     * @see com.taskapp.dataaccess.LogDataAccess#save(com.taskapp.model.Log)
     * @param code タスクコード
     * @param status 新しいステータス
     * @param loginUser ログインユーザー
     * @throws AppException タスクコードが存在しない、またはステータスが前のステータスより1つ先でない場合にスローされます
     */
    // public void changeStatus(int code, int status,
    //                         User loginUser) throws AppException {
    // }

    /**
     * タスクを削除します。
     *
     * @see com.taskapp.dataaccess.TaskDataAccess#findByCode(int)
     * @see com.taskapp.dataaccess.TaskDataAccess#delete(int)
     * @see com.taskapp.dataaccess.LogDataAccess#deleteByTaskCode(int)
     * @param code タスクコード
     * @throws AppException タスクコードが存在しない、またはタスクのステータスが完了でない場合にスローされます
     */
    // public void delete(int code) throws AppException {
    // }
}