package stupuid.maga.legacy;

public class ftp {
    static String first = null;
    static String second = null;

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void smain(java.lang.String r20, java.lang.String r21) {
        /*
        r15 = "www.mdane-devportfolio.com";
        r11 = 21;
        r16 = "abc@mdane-devportfolio.com";
        r10 = "12345";
        r7 = new org.apache.commons.net.ftp.FTPClient;
        r7.<init>();
        r7.connect(r15, r11);	 Catch:{ IOException -> 0x007a }
        r0 = r16;
        r7.login(r0, r10);	 Catch:{ IOException -> 0x007a }
        r7.enterLocalPassiveMode();	 Catch:{ IOException -> 0x007a }
        r17 = 2;
        r0 = r17;
        r7.setFileType(r0);	 Catch:{ IOException -> 0x007a }
        r5 = new java.io.File;	 Catch:{ IOException -> 0x007a }
        r0 = r20;
        r5.<init>(r0);	 Catch:{ IOException -> 0x007a }
        r6 = r21;
        r8 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x007a }
        r8.<init>(r5);	 Catch:{ IOException -> 0x007a }
        r17 = java.lang.System.out;	 Catch:{ IOException -> 0x007a }
        r18 = "Start uploading first file";
        r17.println(r18);	 Catch:{ IOException -> 0x007a }
        r3 = r7.storeFile(r6, r8);	 Catch:{ IOException -> 0x007a }
        r8.close();	 Catch:{ IOException -> 0x007a }
        if (r3 == 0) goto L_0x0048;
    L_0x003d:
        r17 = java.lang.System.out;	 Catch:{ IOException -> 0x007a }
        r18 = "The first file is uploaded successfully.";
        r17.println(r18);	 Catch:{ IOException -> 0x007a }
        r17 = "not null";
        first = r17;	 Catch:{ IOException -> 0x007a }
    L_0x0048:
        r13 = new java.io.File;	 Catch:{ IOException -> 0x007a }
        r0 = r20;
        r13.<init>(r0);	 Catch:{ IOException -> 0x007a }
        r14 = r21;
        r8 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x007a }
        r8.<init>(r13);	 Catch:{ IOException -> 0x007a }
        r17 = java.lang.System.out;	 Catch:{ IOException -> 0x007a }
        r18 = "Start uploading second file";
        r17.println(r18);	 Catch:{ IOException -> 0x007a }
        r9 = r7.storeFileStream(r14);	 Catch:{ IOException -> 0x007a }
        r17 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r0 = r17;
        r1 = new byte[r0];	 Catch:{ IOException -> 0x007a }
        r12 = 0;
    L_0x0068:
        r12 = r8.read(r1);	 Catch:{ IOException -> 0x007a }
        r17 = -1;
        r0 = r17;
        if (r12 == r0) goto L_0x00af;
    L_0x0072:
        r17 = 0;
        r0 = r17;
        r9.write(r1, r0, r12);	 Catch:{ IOException -> 0x007a }
        goto L_0x0068;
    L_0x007a:
        r4 = move-exception;
        r17 = java.lang.System.out;	 Catch:{ all -> 0x00dd }
        r18 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00dd }
        r18.<init>();	 Catch:{ all -> 0x00dd }
        r19 = "Error: ";
        r18 = r18.append(r19);	 Catch:{ all -> 0x00dd }
        r19 = r4.getMessage();	 Catch:{ all -> 0x00dd }
        r18 = r18.append(r19);	 Catch:{ all -> 0x00dd }
        r18 = r18.toString();	 Catch:{ all -> 0x00dd }
        r17.println(r18);	 Catch:{ all -> 0x00dd }
        r17 = 0;
        first = r17;	 Catch:{ all -> 0x00dd }
        r17 = 0;
        second = r17;	 Catch:{ all -> 0x00dd }
        r4.printStackTrace();	 Catch:{ all -> 0x00dd }
        r17 = r7.isConnected();	 Catch:{ IOException -> 0x00d8 }
        if (r17 == 0) goto L_0x00ae;
    L_0x00a8:
        r7.logout();	 Catch:{ IOException -> 0x00d8 }
        r7.disconnect();	 Catch:{ IOException -> 0x00d8 }
    L_0x00ae:
        return;
    L_0x00af:
        r8.close();	 Catch:{ IOException -> 0x007a }
        r9.close();	 Catch:{ IOException -> 0x007a }
        r2 = r7.completePendingCommand();	 Catch:{ IOException -> 0x007a }
        if (r2 == 0) goto L_0x00c6;
    L_0x00bb:
        r17 = "not null";
        second = r17;	 Catch:{ IOException -> 0x007a }
        r17 = java.lang.System.out;	 Catch:{ IOException -> 0x007a }
        r18 = "The second file is uploaded successfully.";
        r17.println(r18);	 Catch:{ IOException -> 0x007a }
    L_0x00c6:
        r17 = r7.isConnected();	 Catch:{ IOException -> 0x00d3 }
        if (r17 == 0) goto L_0x00ae;
    L_0x00cc:
        r7.logout();	 Catch:{ IOException -> 0x00d3 }
        r7.disconnect();	 Catch:{ IOException -> 0x00d3 }
        goto L_0x00ae;
    L_0x00d3:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x00ae;
    L_0x00d8:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x00ae;
    L_0x00dd:
        r17 = move-exception;
        r18 = r7.isConnected();	 Catch:{ IOException -> 0x00eb }
        if (r18 == 0) goto L_0x00ea;
    L_0x00e4:
        r7.logout();	 Catch:{ IOException -> 0x00eb }
        r7.disconnect();	 Catch:{ IOException -> 0x00eb }
    L_0x00ea:
        throw r17;
    L_0x00eb:
        r4 = move-exception;
        r4.printStackTrace();
        goto L_0x00ea;
        */
        throw new UnsupportedOperationException("Method not decompiled: stupuid.maga.legacy.ftp.smain(java.lang.String, java.lang.String):void");
    }
}
