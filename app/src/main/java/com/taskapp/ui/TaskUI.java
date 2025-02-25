package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.print.DocFlavor.READER;

import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.model.User;
import com.taskapp.model.Task;
import com.taskapp.App;
import com.taskapp.exception.AppException;
import com.taskapp.dataaccess.*;

public class TaskUI {
    private final BufferedReader reader;

    private final UserLogic userLogic;

    private final TaskLogic taskLogic;

    private User loginUser;

    public TaskUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        userLogic = new UserLogic();
        taskLogic = new TaskLogic();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param reader
     * @param userLogic
     * @param taskLogic
     */
    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) {
        this.reader = reader;
        this.userLogic = userLogic;
        this.taskLogic = taskLogic;
    }

    /**
     * メニューを表示し、ユーザーの入力に基づいてアクションを実行します。
     *
     * @see #inputLogin()
     * @see com.taskapp.logic.TaskLogic#showAll(User)
     * @see #selectSubMenu()
     * @see #inputNewInformation()
     */
    public void displayMenu() {
        System.out.println("タスク管理アプリケーションにようこそ!!");

        inputLogin();
        // メインメニュー
        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                System.out.println();

                switch (selectMenu) {
                    case "1":
                        taskLogic.showAll(loginUser);
                        selectSubMenu();
                        break;
                    case "2":
                        inputNewInformation();
                        break;
                    case "3":
                        System.out.println("ログアウトしました。");
                        flg = false;
                        break;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのログイン情報を受け取り、ログイン処理を行います。
     *
     * @see com.taskapp.logic.UserLogic#login(String, String)
     */
    public void inputLogin() {
        /*
         * 1．無限ループでメールアドレス・パスワードの入力を求める
         * 2．入力されたメールアドレス、パスワードを引数にUserLogicクラスのloginを呼び出しloginUserに代入
         * 3．例外がスローされた場合は例外メッセージを出力し再度入力を求める
         * 4．正常に処理された場合はループを抜け終了
         */
        
        boolean flg = true;
        while (flg) {
            try {
                System.out.print("メールアドレスを入力してください：");
                String email = reader.readLine();
                System.out.print("パスワードを入力してください：");
                            String password = reader.readLine();
    
                loginUser = userLogic.login(email, password);
                flg = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
    }


    /**
     * ユーザーからの新規タスク情報を受け取り、新規タスクを登録します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#save(int, String, int, User)
     */
    public void inputNewInformation() {
        /*
         * 1．タスクコード、タスク名、タスクを担当するユーザーコードの入力を求める
         * 2．各項目に対してバリデーションを行う
         * 3．ユーザーコードが存在しない場合は例外メッセージを出力
         */

        boolean flg = true;
        while (flg) {
            try {
                System.out.print("タスクコードを入力してください：");
                String taskCode = reader.readLine();
                if (!isNumeric(taskCode)) {
                    System.out.println("コードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }

                System.out.print("タスク名を入力してください：");
                String taskName = reader.readLine();
                if (!(taskName.length() <= 10)) {
                    System.out.println("タスク名は10文字以内で入力してください");
                    System.out.println();
                    continue;
                }

                System.out.print("担当するユーザーのコードを選択してください：");
                String userCode = reader.readLine();
                if (!isNumeric(userCode)) {
                    System.out.println("ユーザーのコードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }

                taskLogic.save(Integer.parseInt(taskCode), taskName, Integer.parseInt(userCode), loginUser);
                System.out.println(taskName + "の登録が完了しました");
                flg = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * タスクのステータス変更または削除を選択するサブメニューを表示します。
     *
     * @see #inputChangeInformation()
     * @see #inputDeleteInformation()
     */
    public void selectSubMenu() {
        /*
         * 1．無限ループで選択肢を入力させる
         * 2．1を選択した場合はinputChangeInformation、2を選択した場合はループを抜ける
         */

        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~3から好きな選択肢を選んでください。");
                System.out.println("1. タスクのステータス変更, 2. タスク削除, 3. メインメニューに戻る");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                switch (selectMenu) {
                    case "1":
                        inputChangeInformation();
                        break;
                    case "2":
                        inputDeleteInformation();
                        break;
                    case "3":
                        flg = false;
                        break;
                    default:
                        System.out.println("選択肢が誤っています");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }


    /**
     * ユーザーからのタスクステータス変更情報を受け取り、タスクのステータスを変更します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#changeStatus(int, int, User)
     */
    public void inputChangeInformation() {
        /*
         * 1．無限ループでステータスの変更を選択させる
         * 2．入力値が正しくない場合のバリデーションを行う
         * 3．例外がスローされた場合はメッセージを出力
         * 4．正常に入力が終了したらループを抜ける
         */

        boolean flg = true;
        while (flg) {
            try {
                System.out.print("ステータスを変更するタスクコードを入力してください：");
                String taskCode = reader.readLine();
                if (!isNumeric(taskCode)) {
                    System.out.println("コードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }
                System.out.println("どのステータスに変更するか選択してください。");
                System.out.println("1. 着手中, 2. 完了");
                System.out.print("選択肢：");
                String statusCode = reader.readLine();
                if (!isNumeric(statusCode)) {
                    System.out.println("ステータスは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }
                if (Integer.parseInt(statusCode) != 1 && Integer.parseInt(statusCode) != 2) {
                    System.out.println("ステータスは1・2の中から選択してください");
                    System.out.println();
                    continue;
                }

                taskLogic.changeStatus(Integer.parseInt(taskCode), Integer.parseInt(statusCode), loginUser);
                System.out.println("ステータスの変更が完了しました。");
                flg = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
    }

    /**
     * ユーザーからのタスク削除情報を受け取り、タスクを削除します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#delete(int)
     */
    public void inputDeleteInformation() {
        /*
         * 1．無限ループで入力を求める
         * 2．入力値に併せてバリデーションを行う
         * 
         */

        boolean flg = true;
        while (flg) {
            try {
                System.out.print("削除するタスクコードを入力してください：");
                String taskCode = reader.readLine();
                if (!isNumeric(taskCode)) {
                    System.out.println("コードは半角の数字で入力してください");
                    System.out.println();
                    continue;
                }
                
                TaskDataAccess taskDataAccess = new TaskDataAccess();
                Task task = taskDataAccess.findByCode(Integer.parseInt(taskCode));
                taskLogic.delete(Integer.parseInt(taskCode));
                
                System.out.println(task.getName() + "の削除が完了しました。");
                flg = false;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
            System.out.println();
        }
    }

    /**
     * 指定された文字列が数値であるかどうかを判定します。
     * 負の数は判定対象外とする。
     *
     * @param inputText 判定する文字列
     * @return 数値であればtrue、そうでなければfalse
     */
    public boolean isNumeric(String inputText) {
        return inputText.chars().allMatch(c -> Character.isDigit((char) c));
    }
}