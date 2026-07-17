INSERT INTO system_roles (`code`, `name`) VALUES ('SYSTEM_ADMIN', 'システム管理者');
INSERT INTO system_roles (`code`, `name`) VALUES ('USER', '一般ユーザー');

INSERT INTO project_roles (`code`, `name`) VALUES ('PROJECT_ADMIN', 'プロジェクト管理者');
INSERT INTO project_roles (`code`, `name`) VALUES ('MEMBER', 'メンバー');
INSERT INTO project_roles (`code`, `name`) VALUES ('VIEWER', '閲覧者');

INSERT INTO task_statuses (`code`, `name`, `sort_order`) VALUES ('TODO', 'ToDo', 1);
INSERT INTO task_statuses (`code`, `name`, `sort_order`) VALUES ('IN_PROGRESS', 'In Progress', 2);
INSERT INTO task_statuses (`code`, `name`, `sort_order`) VALUES ('REVIEW', 'Review', 3);
INSERT INTO task_statuses (`code`, `name`, `sort_order`) VALUES ('DONE', 'Done', 4);

INSERT INTO task_priorities (`code`, `name`, `sort_order`) VALUES ('HIGHEST', 'Highest', 1);
INSERT INTO task_priorities (`code`, `name`, `sort_order`) VALUES ('HIGH', 'High', 2);
INSERT INTO task_priorities (`code`, `name`, `sort_order`) VALUES ('MEDIUM', 'Medium', 3);
INSERT INTO task_priorities (`code`, `name`, `sort_order`) VALUES ('LOW', 'Low', 4);
INSERT INTO task_priorities (`code`, `name`, `sort_order`) VALUES ('LOWEST', 'Lowest', 5);