//서비스 관리자 계정 추가
insert into "user"("id", "account_id", "password", "phone_number", "user_type", "dtype") values (0, 'admin', 'admin123', '010-1234-1234', 'SERVICE_MANAGER', 'SERVICE_MANAGER');
insert into "service_manager"("id", "nickname") values (0, 'admin');