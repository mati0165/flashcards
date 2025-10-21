package com.example.flashcards;

import io.supabase.SupabaseClient;
import io.supabase.SupabaseClientKt;
import io.supabase.gotrue.GoTrue;
import io.supabase.postgrest.Postgrest;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.future.FutureKt;

import java.util.concurrent.CompletableFuture;

public class SupabaseManager {

    private static SupabaseClient client;

    // Inicjalizujemy klienta
    public static synchronized void initialize() {
        if (client == null) {
            String supabaseUrl = BuildConfig.SUPABASE_URL;
            String supabaseKey = BuildConfig.SUPABASE_API_KEY;

            client = SupabaseClientKt.SupabaseClient(supabaseUrl, supabaseKey);
        }
    }

    // Metoda do pobierania instancji klienta
    public static synchronized SupabaseClient getClient() {
        if (client == null) {
            initialize();
        }
        return client;
    }

    // Metoda do pobierania modułu bazy danych (PostgREST)
    public static Postgrest getDb() {
        return getClient().getPostgrest();
    }

    // Metoda do pobierania modułu autoryzacji (GoTrue)
    public static GoTrue getAuth() {
        return getClient().getAuth();
    }

    // PRZYKŁAD: Jak teraz będzie wyglądać pobieranie decków
    public static CompletableFuture<String> fetchDecks() {
        return FutureKt.asCompletableFuture(
                getDb().from("decks").select().execute(false),
                GlobalScope.INSTANCE.getCoroutineContext()
        );
    }

    // PRZYKŁAD: Jak teraz będzie wyglądać pobieranie fiszek
    public static CompletableFuture<String> fetchFlashcardsForDeck(String deckId) {
        return FutureKt.asCompletableFuture(
                getDb().from("flashcards")
                        .select("*")
                        .eq("deck_id", deckId)
                        .execute(false),
                GlobalScope.INSTANCE.getCoroutineContext()
        );
    }
}