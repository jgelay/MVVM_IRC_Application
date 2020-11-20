using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.IO;
using System.Windows;

namespace IRC_ClientApplication
{
    public class Client
    {
        private TcpClient client;
        private string nickName;

        public Client()
        {

        }

        public Boolean Connect(string SERVERIP, int PORT)
        {
            bool state = true;
            try
            {
                this.client = new TcpClient(SERVERIP, PORT);
            }

            catch (SocketException e)
            {
                Console.WriteLine("SocketExcept: {0}", e);
                Console.WriteLine("Please try again");
                state = false;
            }
            return state;
        }

        public Boolean ClientRegistration(NetworkStream stream, string _userName, string )
        {
            string[] reply;
            string input;


            //password registration
            do
            {
                //Console.Write("PASS" + ": ");
                input = ;

                reply = ParsedMessage(stream, input);
            } while (!reply[1].Equals("NICK\n"));

            //Console.WriteLine("You are now registered");
            //await ircRunning(stream);

            return;
        }
        public string[] ParsedMessage(NetworkStream stream, string input)
        {
            int bytesRead;
            string[] messageArray;
            byte[] result;

            int byteCount = Encoding.ASCII.GetByteCount(input + 1);
            byte[] sendData = new byte[byteCount];
            byte[] respData = new byte[1];

            sendData = Encoding.ASCII.GetBytes(input + "\n");

            stream.Write(sendData, 0, sendData.Length);

            using (var memstream = new MemoryStream())
            {
                while (!(Encoding.ASCII.GetChars(respData)[0] == '\n'))
                {
                    bytesRead = stream.Read(respData, 0, respData.Length);
                    memstream.Write(respData, 0, bytesRead);
                }
                result = memstream.GetBuffer();
                messageArray = Encoding.ASCII.GetString(result, 0, Convert.ToInt32(memstream.Length)).Split(' ');
            }

            return messageArray;
        }

        private async Task Sender(NetworkStream stream)
        {
            try
            {
                string consoleInput = await Task.Run(() => Console.ReadLine());

                string input = ":" + nickName + " " + consoleInput;

                int byteCount = Encoding.ASCII.GetByteCount(input + 1);
                byte[] sendData = new byte[byteCount];
                byte[] respData = new byte[1];

                sendData = Encoding.ASCII.GetBytes(input + "\r\n");

                await stream.WriteAsync(sendData, 0, sendData.Length);
            }
            catch (AggregateException e)
            {
                Console.WriteLine(e);
            }

            //Console.WriteLine("Successfully sent message");
            return;

        }

        private async Task<String> Receiver(NetworkStream stream)
        {
            byte[] respData = new byte[1024];
            int bytesRead;
            StringBuilder message = new StringBuilder();

            do
            {
                bytesRead = await stream.ReadAsync(respData, 0, respData.Length);
                message.AppendFormat("{0}", Encoding.ASCII.GetString(respData, 0, bytesRead));

            } while (stream.DataAvailable);

            Console.Write(message);
            return message.ToString();
        }

        private async Task ircRunning(NetworkStream stream)
        {
            while (true)
            {
                try
                {
                    await Sender(stream);
                }
                catch (AggregateException e)
                {
                    Console.WriteLine(e);
                }

                _ = await Receiver(stream);
            }
        }



        public TcpClient GetClient()
        {
            return this.client;
        }


    }
}
