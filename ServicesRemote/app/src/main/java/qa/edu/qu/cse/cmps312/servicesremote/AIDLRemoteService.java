package qa.edu.qu.cse.cmps312.servicesremote;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import qa.edu.qu.cse.cmps312.services.aidl.KeyGenerator;

public class AIDLRemoteService extends Service {

    // Set of already assigned IDs
    // Note: These keys are not guaranteed to be unique if the Service is killed
    // and restarted.

    private final static Set<UUID> mIDs = new HashSet<UUID>();

    // Implement the Stub for this Object
    private final KeyGenerator.Stub mBinder = new KeyGenerator.Stub() {

        // Implement the remote AIDL method
        public String getKey() {

            UUID id;

            // Acquire lock to ensure exclusive access to mIDs
            // Then examine and modify mIDs

            synchronized (mIDs) {

                do {

                    id = UUID.randomUUID();

                } while (mIDs.contains(id));

                mIDs.add(id);
            }
            return id.toString();
        }
    };

    // Return the Stub defined above
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}