import os
import tkinter as tk

from tkinter import ttk
from tkinter import scrolledtext
from ftplib import FTP

USER = 'Anonymous'
PASSWORD = ''

LOCALHOST = '127.0.0.1'
LOCAL_DOWNLOADS_DIR = 'downloads'


class Application(tk.Frame):
    def __init__(self, master=None):
        super().__init__(master)
        self.master = master
        self.pack()
        self.create_widgets()

    def create_widgets(self):
        self.textbox_recv_msg = scrolledtext.ScrolledText(self, width=50, height=5)
        self.textbox_recv_msg.grid(row=0, column=0, padx=5, pady=5)

        ttk.Label(self, text='Address:').grid(
            row=0, column=1, sticky='NW', padx=5, pady=5
        )
        self.entry_address = ttk.Entry(self, width=20)
        self.entry_address.grid(row=0, column=2, sticky='NW', padx=5, pady=5)
        self.entry_address.insert(0, LOCALHOST)

        ttk.Label(self, text='Account:').grid(
            row=0, column=1, sticky='W', padx=5, pady=5
        )
        self.entry_account = ttk.Entry(self, width=20)
        self.entry_account.grid(row=0, column=2, sticky='W', padx=5, pady=5)
        self.entry_account.insert(0, USER)

        ttk.Label(self, text='Password:').grid(
            row=0, column=1, sticky='SW', padx=5, pady=5
        )
        self.entry_password = ttk.Entry(self, width=20)
        self.entry_password.grid(row=0, column=2, sticky='SW', padx=5, pady=5)
        self.entry_password.insert(0, PASSWORD)

        button_connect = ttk.Button(
            self, text='Connect', width=10, command=self.connect
        )
        button_connect.grid(row=1, column=0, sticky='W', rowspan=5)

        button_login = ttk.Button(self, text='Login', width=10, command=self.login)
        button_login.grid(row=1, column=0, sticky='', rowspan=5)

        button_list = ttk.Button(self, text='List', width=10, command=self.list)
        button_list.grid(row=1, column=0, sticky='E', rowspan=5)

        button_download = ttk.Button(
            self, text='Download', width=10, command=self.download
        )
        button_download.grid(row=1, column=1, sticky='W', rowspan=5)

        button_quit = ttk.Button(self, text='Quit', width=10, command=self.quit)
        button_quit.grid(row=1, column=2, sticky='E', rowspan=5)

    def connect(self):
        self.ftp = FTP(self.entry_address.get())
        self.ftp.set_debuglevel(1)

        self.textbox_recv_msg.insert(tk.END, '{}\n'.format(self.ftp.getwelcome()))

    def login(self):
        login_resp = self.ftp.login(
            user=self.entry_account.get(), passwd=self.entry_password.get()
        )
        print(login_resp[0:3])
        # 230: Success
        if login_resp[0:3] == '230':
            self.textbox_recv_msg.insert(
                tk.END, 'Success logged in as {}\n'.format(self.entry_account.get())
            )

    def list(self):
        self.ftp.retrlines('LIST')
        files_list = [i for i in self.ftp.nlst() if i not in ['.', '..']]

        print('文件列表: ', files_list)
        self.textbox_recv_msg.insert(tk.END, '\n文件列表: {}\n'.format(files_list))
        self.files_list = files_list

    def download(self):
        if not os.path.exists(LOCAL_DOWNLOADS_DIR):
            os.makedirs(LOCAL_DOWNLOADS_DIR)
        for remote_file in self.files_list:
            with open('{}/{}'.format(LOCAL_DOWNLOADS_DIR, remote_file), 'wb') as fp:
                self.ftp.retrbinary('RETR {}'.format(remote_file), fp.write, 1024)

        self.textbox_recv_msg.insert(
            tk.END, '\n下载文件成功! 文件存放在{}文件夹下\n'.format(LOCAL_DOWNLOADS_DIR)
        )

    def quit(self):
        if hasattr(self, 'ftp'):
            self.ftp.quit()
        self.master.destroy()


if __name__ == '__main__':
    root = tk.Tk()
    root.title('Easy FTP Client')
    root.resizable(0, 0)

    app = Application(root)
    app.mainloop()
