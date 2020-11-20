using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;

namespace IRC_ClientApplication
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        protected override void OnStartup(StartupEventArgs e)
        {
            //base.OnStartup(e);
            //ConnectView connectView = new ConnectView();
            //RegisterView registerView = new RegisterView();
            MainWindowView mainwindowview = new MainWindowView();
            var viewModel = new MainWindowViewModel();
            mainwindowview.DataContext = viewModel;
            mainwindowview.Show();
        }
    }
}
