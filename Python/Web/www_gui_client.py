import re
import threading
import tkinter as tk
from tkinter import ttk
from tkinter import scrolledtext
from socket import socket, AF_INET, SOCK_STREAM

DEFAULT_URL = 'www.baidu.com'


class Application(tk.Frame):
    def __init__(self, master=None):
        super().__init__(master)
        self.master = master
        self.pack()
        self.create_widgets()
        self.is_connected = False

    def create_widgets(self):
        self.textbox_recv_msg = scrolledtext.ScrolledText(self, width=50, height=5)
        self.textbox_recv_msg.grid(row=0, column=0, padx=5, pady=5)

        self.textbox_parsed_msg = scrolledtext.ScrolledText(self, width=50, height=5)
        self.textbox_parsed_msg.grid(row=1, column=0, padx=5, pady=5)

        ttk.Label(self, text='URL:').grid(row=0, column=1, sticky='NW', padx=5, pady=5)
        self.entry_url = ttk.Entry(self, width=30)
        self.entry_url.grid(row=0, column=2, sticky='NW', padx=5, pady=5)
        self.entry_url.insert(0, DEFAULT_URL)

        button_browser = ttk.Button(
            self, text='Browser', width=10, command=self.browser
        )
        button_browser.grid(row=0, column=1, sticky='SW', rowspan=5)

        button_parse = ttk.Button(self, text='Parse', width=10, command=self.parse)
        button_parse.grid(row=0, column=2, sticky='SW', rowspan=5)

        button_quit = ttk.Button(self, text='Quit', width=10, command=self.quit)
        button_quit.grid(row=0, column=2, sticky='SE', rowspan=5)

    def connect(self):
        url = self.entry_url.get()
        regex_url = re.findall(r'(.*?)/(.*)', url)
        print(regex_url)
        if len(regex_url) == 0:
            self.url_path = ''
            self.addr_info = (url, 80)
        else:
            self.url_path = regex_url[0][1]
            self.addr_info = (regex_url[0][0], 80)
        # AF_INET: IPv4
        # SOCK_STREAM: TCP
        self.tcp_client = socket(AF_INET, SOCK_STREAM)

        thread = threading.Thread(target=self.on_connect)
        thread.start()

    def on_connect(self):
        self.tcp_client.connect(self.addr_info)
        self.is_connected = True
        print(f'连接服务器({self.addr_info[0]})成功')
        self.listen_recv_msg()

    def listen_recv_msg(self):
        recv_thread = threading.Thread(target=self.recv_msg)
        recv_thread.start()

    def recv_msg(self):
        line_break = '\r\n'
        request_header = 'GET /{} HTTP/1.1'.format(self.url_path) + line_break
        request_header += 'Host: ' + self.addr_info[0] + line_break * 2

        self.tcp_client.send(request_header.encode('utf-8'))
        while True:
            str_recv = self.tcp_client.recv(1024).decode('utf-8')
            if 'HTTP/1.1 200 OK' in str_recv:
                print('200')
            if str_recv:
                self.textbox_recv_msg.insert(tk.END, f'{str_recv}')

    def browser(self):
        # 清空文本框内容
        self.textbox_recv_msg.delete('1.0', tk.END)
        self.connect()

    def parse(self):
        self.textbox_parsed_msg.delete('1.0', tk.END)

        html_text = self.textbox_recv_msg.get('0.0', tk.END)
        html_text = html_text.split('\r\n\r\n')
        # 获取HTML
        for block in html_text:
            if '<html>' in block:
                html_text = block
        title = re.findall(r'<title>(.*?)</title>', html_text)
        print(title)
        self.textbox_parsed_msg.insert(tk.END, f'HTML标题: {title[0]}')

    def disconnect(self):
        if self.is_connected:
            self.tcp_client.close()
            self.is_connected = False

    def quit(self):
        self.disconnect()
        self.master.destroy()


if __name__ == '__main__':
    root = tk.Tk()
    root.title('Easy WWW Client')
    root.resizable(0, 0)

    app = Application(root)
    app.mainloop()
