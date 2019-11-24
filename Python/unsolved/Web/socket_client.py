import threading
import tkinter as tk
from tkinter import ttk
from tkinter import scrolledtext
from socket import socket, AF_INET, SOCK_STREAM


class Application(tk.Frame):
    def __init__(self, master=None):
        super().__init__(master)
        self.master = master
        self.pack()
        self.create_widgets()

    def create_widgets(self):
        self.scrolled_text_recv_msg = scrolledtext.ScrolledText(
            self, width=30, height=5)
        self.scrolled_text_recv_msg.grid(row=0, column=0, padx=5, pady=5)

        ttk.Label(self, text='IP:').grid(
            row=0, column=1, sticky='N', padx=5, pady=5)
        self.entry_ip = ttk.Entry(self)
        self.entry_ip.grid(row=0, column=2, sticky='N', padx=5, pady=5)
        self.entry_ip.insert(0, '127.0.0.1')

        ttk.Label(self, text='Port:').grid(
            row=0, column=1, sticky='S', padx=5, pady=5)
        self.entry_port = ttk.Entry(self)
        self.entry_port.grid(row=0, column=2, sticky='S', padx=5, pady=5)
        self.entry_port.insert(0, '4555')

        self.entry_chatbox = ttk.Entry(self)
        self.entry_chatbox.grid(row=1, column=0, sticky='W', padx=5, pady=5)
        self.entry_chatbox.focus()

        button_send = ttk.Button(self, text='Send', command=self.send)
        button_send.grid(row=1, column=0, sticky='E', rowspan=5)

        button_connect = ttk.Button(
            self, text='Connect', command=self.connect)
        button_connect.grid(row=1, column=1, sticky='W', rowspan=5)

        button_quit = ttk.Button(
            self, text='Quit', command=self.master.destroy)
        button_quit.grid(row=1, column=2, sticky='W', rowspan=5)

    def send(self):
        self.client_socket.send(self.entry_chatbox.get().encode('utf-8'))

    def connect(self):
        self.addr_info = (self.entry_ip.get(), int(self.entry_port.get()))
        # AF_INET: IPv4
        # SOCK_STREAM: TCP
        self.client_socket = socket(AF_INET, SOCK_STREAM)
        thread = threading.Thread(target=self.on_connect)
        thread.start()

    def on_connect(self):
        self.client_socket.connect(self.addr_info)

    def disconnect(self):
        self.client_socket.close()


if __name__ == '__main__':
    root = tk.Tk()
    root.title('Easy TCP Client')
    root.resizable(0, 0)

    app = Application(root)
    app.mainloop()
