package com.example.kelolauser;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00032\b\b\u0001\u0010\u0005\u001a\u00020\tH\'\u00a8\u0006\n"}, d2 = {"Lcom/example/kelolauser/ApiService;", "", "login", "Lretrofit2/Call;", "Lcom/example/kelolauser/LoginResponse;", "request", "Lcom/example/kelolauser/LoginRequest;", "register", "Lcom/example/kelolauser/RegisterResponse;", "Lcom/example/kelolauser/RegisterRequest;", "app_debug"})
public abstract interface ApiService {
    
    @retrofit2.http.POST(value = "api/auth/login")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.kelolauser.LoginResponse> login(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.kelolauser.LoginRequest request);
    
    @retrofit2.http.POST(value = "api/auth/register")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.kelolauser.RegisterResponse> register(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.kelolauser.RegisterRequest request);
}