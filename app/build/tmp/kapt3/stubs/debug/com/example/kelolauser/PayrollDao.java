package com.example.kelolauser;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\'J\u000e\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\'J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u0006H\'\u00a8\u0006\t"}, d2 = {"Lcom/example/kelolauser/PayrollDao;", "", "deleteAll", "", "getAll", "", "Lcom/example/kelolauser/Payroll;", "insert", "payroll", "app_debug"})
@androidx.room.Dao()
public abstract interface PayrollDao {
    
    @androidx.room.Query(value = "SELECT * FROM payroll")
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.example.kelolauser.Payroll> getAll();
    
    @androidx.room.Insert()
    public abstract void insert(@org.jetbrains.annotations.NotNull()
    com.example.kelolauser.Payroll payroll);
    
    @androidx.room.Query(value = "DELETE FROM payroll")
    public abstract void deleteAll();
}