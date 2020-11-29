using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Collections.Specialized;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;

namespace IRC_ClientApplication
{
    public class MainWindowViewModel : ViewModelBase
    {
        #region Fields
        ObservableCollection<string> _messages;
        private string _message;
        private ICommand _sendMessage;
        private string _server;
        private int _port;
        private string _nickname;
        private string _realname;
        private string _username;
        private string _password;
        private ICommand _connect;
        private ICommand _register;
        private Client client;
        private NetworkStream stream;
        private ViewModelBase _viewModel = new ConnectViewModel();
        #endregion // fields

        #region Properties
        public ViewModelBase ViewModel
        {
            get { return _viewModel; }
            set
            {
                _viewModel = value;
                OnPropertyChanged(nameof(ViewModel));
            }
        }
        public ObservableCollection<string> Messages
        {
            get
            {
                if (_messages == null)
                {
                    _messages = new ObservableCollection<string>();
                    _messages.CollectionChanged += this.OnMessagesChanged;
                }
                return _messages;
            }

        }

        public string Message
        {
            get
            {
                return _message;
            }
            set
            {
                _message = value;
                OnPropertyChanged("Message");
            }
        }

        public string Server
        {
            get
            {
                return _server;
            }
            set
            {
                _server = value;
                OnPropertyChanged("Server");
            }
        }

        public int Port
        {
            get
            {
                return _port;
            }
            set
            {
                _port = value;
                OnPropertyChanged("Port");
            }
        }

        public string Realname
        {
            get
            {
                return _realname;
            }
            set
            {
                _realname = value;
                OnPropertyChanged("Realname");
            }
        }

        public string Nickname
        {
            get
            {
                return _nickname;
            }
            set
            {
                _nickname = value;
                OnPropertyChanged("Nickname");
            }
        }

        public string Username
        {
            get
            {
                return _username;
            }
            set
            {
                _username = value;
                OnPropertyChanged("Username");
            }
        }

        public string Password
        {
            get
            {
                return _password;
            }
            set
            {
                _password = value;
                OnPropertyChanged("Password");
            }
        }

        #endregion // properties


        #region Constructor
        public MainWindowViewModel()
        {
            this.client = new Client();
        }
        #endregion //Constructor

        

        #region Commands
        public ICommand SendMessage
        {
            get
            {
                return _sendMessage ??= new RelayCommand(
                    x =>
                    {
                        ShowMessage();
                    });
            }
        }

        public ICommand Connect
        {
            get
            {
                return _connect ??= new RelayCommand(
                    x =>
                    {
                        ConnectToServer();
                    });
            }
        }

        public ICommand Register
        {
            get
            {
                return _register ??= new RelayCommand(
                    x =>
                    {
                        RegisterToServer();
                    });
            }
        }

        #endregion // Commands

        private void ShowMessage()
        {
            //MessageBox.Show(_message);
            this._messages.Add(_message);
        }

        private void ConnectToServer()
        {
            if (this.client.Connect(_server, _port))
            {
                this.stream = client.GetClient().GetStream();
                ViewModel = new RegisterViewModel();
            }
            
        }

        private void RegisterToServer()
        {
           if (this.client.ClientRegistration(this.stream, _username, _nickname, _password))
            {
                ViewModel = new ChatViewModel();
            }
           
        }

        void OnMessagesChanged(object sender, NotifyCollectionChangedEventArgs e)
        {

        }
    }
}
