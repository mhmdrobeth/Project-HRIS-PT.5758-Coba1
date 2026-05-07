import os

drawable_dir = r"app\src\main\res\drawable"
os.makedirs(drawable_dir, exist_ok=True)

def create_vector(filename, path_data, fill_color="#718096"):
    content = f'''<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
  <path
      android:fillColor="{fill_color}"
      android:pathData="{path_data}"/>
</vector>'''
    with open(os.path.join(drawable_dir, filename), "w") as f:
        f.write(content)

def create_shape(filename, bg_color, radius="8dp"):
    content = f'''<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="{bg_color}" />
    <corners android:radius="{radius}" />
</shape>'''
    with open(os.path.join(drawable_dir, filename), "w") as f:
        f.write(content)

# Vectors (Using generic circle or simple paths for simplicity, but trying to match)
create_vector("ic_dashboard.xml", "M3,13h8V3H3V13z M3,21h8v-6H3V21z M13,21h8V11h-8V21z M13,3v6h8V3H13z")
create_vector("ic_absensi.xml", "M11.99,2C6.47,2 2,6.48 2,12s4.47,10 9.99,10C17.52,22 22,17.52 22,12S17.52,2 11.99,2zM12,20c-4.42,0 -8,-3.58 -8,-8s3.58,-8 8,-8 8,3.58 8,8 -3.58,8 -8,8zM12.5,7H11v6l5.25,3.15 0.75,-1.23 -4.5,-2.67z")
create_vector("ic_history.xml", "M13,3c-4.97,0 -9,4.03 -9,9H1l3.89,3.89 0.07,0.14L9,12H6c0,-3.87 3.13,-7 7,-7s7,3.13 7,7 -3.13,7 -7,7c-1.93,0 -3.68,-0.79 -4.94,-2.06l-1.42,1.42C8.27,19.99 10.51,21 13,21c4.97,0 9,-4.03 9,-9s-4.03,-9 -9,-9zM12,8v5l4.28,2.54 0.72,-1.21 -3.5,-2.08V8H12z")
create_vector("ic_add.xml", "M19,13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z", "#FFFFFF")
create_vector("ic_izin.xml", "M19,3h-4.18C14.4,1.84 13.3,1 12,1c-1.3,0 -2.4,0.84 -2.82,2H5C3.9,3 3,3.9 3,5v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2V5C21,3.9 20.1,3 19,3zM12,3c0.55,0 1,0.45 1,1s-0.45,1 -1,1 -1,-0.45 -1,-1 0.45,-1 1,-1zM14,17H7v-2h7v2zM17,13H7v-2h10v2zM17,9H7V7h10v2z")
create_vector("ic_admin.xml", "M12,12c2.21,0 4,-1.79 4,-4s-1.79,-4 -4,-4 -4,1.79 -4,4 1.79,4 4,4zM12,14c-2.67,0 -8,1.34 -8,4v2h16v-2c0,-2.66 -5.33,-4 -8,-4z")
create_vector("ic_users.xml", "M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z", "#00A97E")
create_vector("ic_settings.xml", "M19.14,12.94c0.04,-0.3 0.06,-0.61 0.06,-0.94c0,-0.32 -0.02,-0.64 -0.06,-0.94l2.03,-1.58c0.18,-0.14 0.23,-0.41 0.12,-0.61l-1.92,-3.32c-0.12,-0.22 -0.37,-0.29 -0.59,-0.22l-2.39,0.96c-0.5,-0.38 -1.03,-0.7 -1.62,-0.94L14.4,2.81c-0.04,-0.24 -0.24,-0.41 -0.48,-0.41h-3.84c-0.24,0 -0.43,0.17 -0.47,0.41L9.25,5.35C8.66,5.59 8.12,5.92 7.63,6.29L5.24,5.33c-0.22,-0.08 -0.47,0 -0.59,0.22L2.73,8.87C2.62,9.08 2.66,9.34 2.86,9.48l2.03,1.58C4.84,11.36 4.8,11.69 4.8,12s0.02,0.64 0.06,0.94l-2.03,1.58c-0.18,0.14 -0.23,0.41 -0.12,0.61l1.92,3.32c0.12,0.22 0.37,0.29 0.59,0.22l2.39,-0.96c0.5,0.38 1.03,0.7 1.62,0.94l0.36,2.54c0.05,0.24 0.24,0.41 0.48,0.41h3.84c0.24,0 0.43,-0.17 0.47,-0.41l0.36,-2.54c0.59,-0.24 1.13,-0.56 1.62,-0.94l2.39,0.96c0.22,0.08 0.47,0 0.59,-0.22l1.92,-3.32c0.12,-0.22 0.07,-0.49 -0.12,-0.61L19.14,12.94zM12,15.6c-1.98,0 -3.6,-1.62 -3.6,-3.6s1.62,-3.6 3.6,-3.6s3.6,1.62 3.6,3.6S13.98,15.6 12,15.6z")
create_vector("ic_search.xml", "M15.5 14h-.79l-.28-.27C15.41 12.59 16 11.11 16 9.5 16 5.91 13.09 3 9.5 3S3 5.91 3 9.5 5.91 16 9.5 16c1.61 0 3.09-.59 4.23-1.57l.27.28v.79l5 4.99L20.49 19l-4.99-5zm-6 0C7.01 14 5 11.99 5 9.5S7.01 5 9.5 5 14 7.01 14 9.5 11.99 14 9.5 14z")
create_vector("ic_whatsapp.xml", "M12.04 2c-5.46 0-9.91 4.45-9.93 9.91-.01 1.74.45 3.45 1.3 4.95L2 22l5.25-1.38c1.45.8 3.09 1.22 4.79 1.22 5.46 0 9.91-4.45 9.93-9.91.01-5.46-4.45-9.91-9.93-9.93zm5.42 14.15c-.23.64-1.31 1.23-1.83 1.28-.47.05-1.07.13-3.21-.75-2.58-1.07-4.22-3.7-4.35-3.87-.13-.17-1.04-1.38-1.04-2.63 0-1.25.65-1.87.89-2.12.22-.24.49-.31.65-.31.17 0 .34 0 .49.01.16 0 .37-.06.56.42.21.52.71 1.73.77 1.87.06.13.1.29.02.46-.08.17-.13.27-.24.4-.11.13-.24.28-.33.39-.11.11-.23.23-.1.46.12.22.54.91 1.16 1.46.8.71 1.48.93 1.69 1.04.22.11.35.09.48-.06.13-.15.56-.65.71-.87.15-.22.3-.18.5-.11.2.06 1.29.61 1.51.72.22.11.37.17.42.26.06.1.06.56-.16 1.2z", "#25D366")
create_vector("ic_email.xml", "M20 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 4l-8 5-8-5V6l8 5 8-5v2z", "#00B0FF")
create_vector("ic_edit.xml", "M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z", "#00A97E")
create_vector("ic_delete.xml", "M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z", "#FF5A5F")

# Shapes
create_shape("bg_rounded_white.xml", "#FFFFFF")
create_shape("bg_rounded_search.xml", "#F8F9FA", "20dp")
create_shape("bg_rounded_menu_active.xml", "#EAF6F3", "8dp")
create_shape("bg_tag_admin.xml", "#00B0FF", "4dp")
create_shape("bg_tag_user.xml", "#2D3748", "4dp")
create_shape("bg_tag_status.xml", "#25D366", "12dp")
create_shape("bg_button_add.xml", "#00A97E", "8dp")

print("Drawables generated.")
