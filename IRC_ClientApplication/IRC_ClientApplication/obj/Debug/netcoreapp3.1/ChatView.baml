   M S B A M L   `   `   `  ����P  LIRC_ClientApplication, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null=#clr-namespace:IRC_ClientApplicationIRC_ClientApplication  5      $    IRC_ClientApplication.ChatView   5      \ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35e aSystem.Windows.Controls.Ribbon, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35F 9http://schemas.microsoft.com/winfx/2006/xaml/presentation     5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      5local#clr-namespace:IRC_ClientApplication     5      $	��450��5   3   .��5      �   � RowDefinitions  5	   
   �� 5
      $
��279* �6   �� 5      $
��119* �6   �� 5      $��2* �6   .+�5   
   �� $
��Left=�6   $	��450��6,   $	��Top=�69   $	��792��6Q    J� RenderTransformOrigin$ 1.832,1.482&�6]   
�������$��3��6�   .+�5      �� $	��450��6   $	��239��6"   
����p��6.   �� 5      -   5         ��Name$ chatListView��5      $	��381��6-    �� Left$	 246��6:   $	��536��6L   
 �� Top$ 10��6�   ��6X   �� *DataContext.Messages+ �� RelativeSource ��  �� AncestorType*   $IRC_ClientApplication.MainWindowView"   �� Mode$ OneWay=�5      5      �� 5   
   -   5         ��Name$ textBox��5      $
��Left=�6#   $��42��6>   	 �� TextWrapping$
	 Wrap=�6J   $	��Top=�6�   $	��460��6�   $��
246,84,0,0q�6  
���   �$��1��60  ��6^   �� *DataContext.Message+ �� "  $ OneWayToSource=�60  �� 5   
   -   5        
 ��Name$
 button��5      $��Button��6!   $
��Left=�62   $	��Top=�6M   $��71��6e   $��
706,84,0,0q�6p   $��42��6�   $��1��6  ��6�   �� *DataContext.SendMessage+ �� "  $ OneWay=�6  5      5      